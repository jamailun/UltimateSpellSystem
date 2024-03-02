package fr.jamailun.ultimatespellsystem.bukkit.spells;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface SpellEntity {

    UUID getUniqueId();

    Optional<Entity> getBukkitEntity();

    default boolean isBukkit() {
        return getBukkitEntity().isPresent();
    }

    @NotNull Location getLocation();

    void teleport(@NotNull Location location);

    void sendMessage(Component component);

    void remove();

    boolean isValid();

    void addPotionEffect(PotionEffect effect);
}
