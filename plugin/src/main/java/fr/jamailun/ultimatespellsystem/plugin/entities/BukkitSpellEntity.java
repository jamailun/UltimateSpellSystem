package fr.jamailun.ultimatespellsystem.plugin.entities;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.errors.InvalidTypeException;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BukkitSpellEntity implements SpellEntity {

    private final Entity entity;

    @Override
    public @NotNull UUID getUniqueId() {
        return entity.getUniqueId();
    }

    @Override
    public @NotNull Optional<Entity> getBukkitEntity() {
        return Optional.of(entity);
    }

    @Override
    public @NotNull Location getLocation() {
        return entity.getLocation();
    }

    @Override
    public @NotNull Location getEyeLocation() {
        if(entity instanceof LivingEntity living)
            return living.getEyeLocation();
        return entity.getLocation();
    }

    @Override
    public void teleport(@NotNull Location location) {
        entity.teleport(location);
    }

    @Override
    public void sendMessage(Component component) {
        entity.sendMessage(component);
    }

    @Override
    public void remove() {
        entity.remove();
    }

    @Override
    public boolean isValid() {
        return entity.isValid() && ! entity.isDead();
    }

    @Override
    public void addPotionEffect(PotionEffect effect) {
        if(entity instanceof LivingEntity living) {
            living.addPotionEffect(effect);
        }
    }

    @Override
    public void setVelocity(@NotNull Vector vector) {
        getBukkitEntity().ifPresent(entity -> entity.setVelocity(vector));
    }

    @Override
    public boolean equals(Object other) {
        if(other == null) return false;
        if(this == other) return true;
        UUID otherUuid = switch (other) {
            case Entity otherEn -> otherEn.getUniqueId();
            case SpellEntity otherSe -> otherSe.getUniqueId();
            default -> throw new InvalidTypeException("compare SpellEntity", "entity", other);
        };
        return Objects.equals(otherUuid, getUniqueId());
    }

    @Override
    public int hashCode() {
        return getUniqueId().hashCode();
    }

    @Override
    public String toString() {
        return entity.getName();
    }
}
