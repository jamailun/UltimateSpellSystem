package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.UssKeys;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class SendAttributeNode extends RuntimeStatement {

    /**
     * Even if the attributes are transient, we want to clear, for instance, the ones of a leaving player.
     */
    private static final Map<UUID, Set<AttributeEntry>> GIVEN_ATTRIBUTES = new ConcurrentHashMap<>();

    private final RuntimeExpression targetRef;
    private final RuntimeExpression numericValue;
    private final RuntimeExpression attributeType;
    private final @Nullable RuntimeExpression modeRef;
    private final RuntimeExpression durationRef;

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        // Type
        Attribute attribute = getAttribute(runtime);

        // Duration
        Duration duration = runtime.safeEvaluate(durationRef, Duration.class);
        Objects.requireNonNull(duration, "Duration should not be null.");

        // Apply modifier
        AttributeModifier modifier = createModifier(runtime);
        List<SpellEntity> targets = runtime.safeEvaluateAcceptsList(targetRef, SpellEntity.class);
        for(SpellEntity target : targets) {
            target.getBukkitEntity().map(Attributable.class::cast)
                    .map(a -> getInstance(a, attribute))
                    .ifPresent(ai -> {
                        ai.addTransientModifier(modifier);
                        GIVEN_ATTRIBUTES.putIfAbsent(target.getUniqueId(), new HashSet<>());
                        GIVEN_ATTRIBUTES.get(target.getUniqueId()).add(new AttributeEntry(attribute, modifier));
                    });
        }

        // Clear after duration
        UltimateSpellSystem.runTaskLater(() -> {
            for(SpellEntity target : targets) {
                target.getBukkitEntity().map(Attributable.class::cast)
                        .map(a -> getInstance(a, attribute))
                        .ifPresent(ai -> {
                            ai.removeModifier(modifier);
                            GIVEN_ATTRIBUTES.get(target.getUniqueId()).removeIf(m -> m.modifier().equals(modifier));
                        });
            }
        }, duration.toTicks());
    }

    private @NotNull Attribute getAttribute(@NotNull SpellRuntime runtime) {
        String attributeName = runtime.safeEvaluate(attributeType, String.class).toUpperCase();
        for(Attribute attribute : Attribute.values()) {
            if(attributeName.equals(attribute.name()))
                return attribute;
            if(attribute.name().endsWith(attributeName))
                return attribute;
        }
        throw new RuntimeException("Unknown attribute: '" + attributeName + "'.");
    }

    public static void purge() {
        for(Map.Entry<UUID, Set<AttributeEntry>> entry : GIVEN_ATTRIBUTES.entrySet()) {
            Entity entity = Bukkit.getEntity(entry.getKey());
            if(entity instanceof Attributable attributable) {
                entry.getValue().forEach(m ->
                        Optional.ofNullable(attributable.getAttribute(m.attribute()))
                                .ifPresent(ai -> ai.removeModifier(m.modifier()))
                );
            }
        }
        GIVEN_ATTRIBUTES.clear();
    }

    public static void purge(@NotNull LivingEntity entity) {
        Set<AttributeEntry> entries = GIVEN_ATTRIBUTES.get(entity.getUniqueId());
        if(entries == null) return;
        entries.forEach(m ->
                Optional.ofNullable(entity.getAttribute(m.attribute()))
                        .ifPresent(ai -> ai.removeModifier(m.modifier()))
        );
    }

    private static @Nullable AttributeInstance getInstance(@NotNull Attributable attributable, @NotNull Attribute attribute) {
        AttributeInstance instance = attributable.getAttribute(attribute);
        if(instance != null)
            return instance;
        attributable.registerAttribute(attribute);
        return attributable.getAttribute(attribute);
    }

    private @NotNull AttributeModifier createModifier(@NotNull SpellRuntime runtime) {
        // Value
        double amount = runtime.safeEvaluate(numericValue, Double.class);

        // Attribute operation
        AttributeModifier.Operation ope;
        if(modeRef == null) {
            ope = AttributeModifier.Operation.ADD_NUMBER;
        } else {
            String opeName = runtime.safeEvaluate(modeRef, String.class);
            ope = AttributeModifier.Operation.valueOf(opeName.toUpperCase());
        }

        // Build using a deprecated API
        return new AttributeModifier(UssKeys.random(), amount, ope);
    }

    @Override
    public @NotNull String toString() {
        return "SEND TO " + targetRef + " ATTRIBUTE "
                + numericValue + " " + attributeType
                + (modeRef == null ? "" : " " + modeRef)
                + " FOR " + durationRef;
    }

    private record AttributeEntry(Attribute attribute, AttributeModifier modifier) {}
}
