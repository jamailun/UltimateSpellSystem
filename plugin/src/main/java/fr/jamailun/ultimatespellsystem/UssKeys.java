package fr.jamailun.ultimatespellsystem;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Exposes keys.
 */
public final class UssKeys {

    /**
     * Set to a STRING. ID of the spell to bind.
     */
    private static @Getter NamespacedKey bindKey;

    /**
     * Set to a BOOLEAN. If true, item destroyed on use.
     */
    private static @Getter NamespacedKey bindDestroysKey;

    /**
     * Set to BOOLEAN. If true, object cannot be dropped on death.
     */
    private static @Getter NamespacedKey notDroppableKey;

    /**
     * Set to a DOUBLE. Custom amount of damages for a projectile.
     */
    private static @Getter NamespacedKey projectileDamagesKey;

    private static Plugin plugin;
    static void initialize(@NotNull Plugin plugin) {
        UssKeys.plugin = plugin;
        bindKey = new NamespacedKey(plugin, "spell");
        bindDestroysKey = new NamespacedKey(plugin, "spell.destroy");
        notDroppableKey = new NamespacedKey(plugin, "not-droppable");
        projectileDamagesKey = new NamespacedKey(plugin, "projectile-damage");
    }

    @Contract("-> new")
    public static @NotNull NamespacedKey random() {
       return new NamespacedKey(plugin, UUID.randomUUID().toString());
    }

}
