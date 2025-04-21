package fr.jamailun.examples.citizens;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@TraitName("uss-caster")
public class CasterTrait extends Trait implements SpellEntity {

    private final List<SpellCastRule> rules = new ArrayList<>();

    public CasterTrait() {
        super("uss-caster");
    }

    public void cast(@NotNull Spell spell) {
        spell.castNotCancellable(this);
    }

    public @NotNull List<SpellCastRule> getRules() {
        return rules;
    }

    // -- Citizens NPC

    @Override
    public void load(@NotNull DataKey key) {
        key.getIntegerSubKeys().forEach(dk -> {
            SpellCastRule rule = SpellCastRule.load(dk);
            rules.add(rule);
        });
    }

    @Override
    public void save(@NotNull DataKey key) {
        int i = 0;
        for(SpellCastRule rule : rules) {
            DataKey out = key.getRelative(i);
            rule.saveData(out);
            i++;
        }
    }

    // -- Spell Entity

    @Override
    public @NotNull UUID getUniqueId() {
        return getBukkitEntity()
                .map(Entity::getUniqueId)
                .orElse(getNPC().getUniqueId());
    }

    @Override
    public @NotNull Optional<Entity> getBukkitEntity() {
        if(getNPC().isSpawned())
            return Optional.of(getNPC().getEntity());
        return Optional.empty();
    }

    @Override
    public @NotNull Location getLocation() {
        return getBukkitEntity().map(Entity::getLocation).orElse(getNPC().getStoredLocation());
    }

    @Override
    public @NotNull Location getEyeLocation() {
        return getBukkitEntity().map(LivingEntity.class::cast)
                .map(LivingEntity::getEyeLocation)
                .orElse(getNPC().getStoredLocation());
    }

    @Override
    public void teleport(@NotNull Location location) {
        if(getNPC().isSpawned())
            getNPC().teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override
    public void remove() {
        if(getNPC().isSpawned())
            getNPC().despawn();
    }

    @Override
    public boolean isValid() {
        return getNPC().isSpawned();
    }

    @Override
    public void sendMessage(Component component) {
        // Nothing here
    }

    @Override
    public void addPotionEffect(PotionEffect effect) {
        getBukkitEntity().map(LivingEntity.class::cast)
                .ifPresent(le -> le.addPotionEffect(effect));
    }

    @Override
    public void setVelocity(@NotNull Vector vector) {
        getBukkitEntity().map(LivingEntity.class::cast)
            .ifPresent(le -> le.setVelocity(vector));
    }
}
