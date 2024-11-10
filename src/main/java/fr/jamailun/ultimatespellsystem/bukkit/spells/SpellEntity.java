package fr.jamailun.ultimatespellsystem.bukkit.spells;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface SpellEntity {

    @NotNull UUID getUniqueId();

    @Contract(pure = true)
    @NotNull Optional<Entity> getBukkitEntity();

    default boolean isBukkit() {
        return getBukkitEntity().isPresent();
    }

    @NotNull Location getLocation();

    @NotNull Location getEyeLocation();

    void teleport(@NotNull Location location);

    void sendMessage(Component component);

    void remove();

    boolean isValid();

    void addPotionEffect(PotionEffect effect);
}
