package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.UssKeys;
import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Basic implementation of a send-NBT node.
 */
public class SendNbtNode extends RuntimeStatement {

    private final RuntimeExpression targetRef;
    private final RuntimeExpression nameRef;
    private final RuntimeExpression valueRef;
    private final RuntimeExpression durationRef;

    public SendNbtNode(RuntimeExpression target, RuntimeExpression nameRef, RuntimeExpression valueRef, RuntimeExpression durationRef) {
        this.targetRef = target;
        this.nameRef = nameRef;
        this.valueRef = valueRef;
        this.durationRef = durationRef;
    }

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        List<SpellEntity> targets = runtime.safeEvaluateAcceptsList(targetRef, SpellEntity.class);

        String name = runtime.safeEvaluate(nameRef, String.class);
        NamespacedKey key = UssKeys.custom(Objects.requireNonNull(name, "Provided name for a send-NBT cannot be null."));
        Duration duration = Objects.requireNonNull(runtime.safeEvaluate(durationRef, Duration.class), "Provided duration for a send-NBT cannot be null.");
        Object value = Objects.requireNonNull(valueRef.evaluate(runtime), "Provided value for a send-NBT cannot be null.");
        UssLogger.logDebug("SET NBT to " + targets + " : [" + key + "] = " + value);

        Consumer<PersistentDataContainer> func = switch(value) {
            case Boolean bool -> nbt -> nbt.set(key, PersistentDataType.BOOLEAN, bool);
            case Double number -> nbt -> nbt.set(key, PersistentDataType.DOUBLE, number);
            case Integer number -> nbt -> nbt.set(key, PersistentDataType.INTEGER, number);
            case String string -> nbt -> nbt.set(key, PersistentDataType.STRING, string);
            default -> throw new RuntimeException("Invalid NBT type: " + value.getClass() + " for value " + value + ".");
        };

        // Add NBT
        targets.forEach(en -> {
            if(en.getNBT() != null)
                func.accept(en.getNBT());
        });

        // Remove it
        UltimateSpellSystem.getScheduler().runTaskLater(() -> targets.forEach(en -> {
            if(en.getNBT() != null)
                en.getNBT().remove(key);
        }), duration.toTicks());
    }

    @Override
    public String toString() {
        return "SEND_NBT(" + targetRef + ", '" + nameRef + "' = " + valueRef + "; for " + durationRef + ")";
    }
}
