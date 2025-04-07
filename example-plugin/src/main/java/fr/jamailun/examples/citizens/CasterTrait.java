package fr.jamailun.examples.citizens;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@TraitName("uss-caster")
public class CasterTrait extends Trait implements SpellEntity {

    public CasterTrait() {
        super("uss-caster");
    }

    public void cast(Spell spell) {
        spell.castNotCancellable(this);
    }

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
}
