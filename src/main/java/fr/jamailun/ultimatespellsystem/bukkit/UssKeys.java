package fr.jamailun.ultimatespellsystem.bukkit;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

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

   static void initialize(@NotNull Plugin plugin) {
       bindKey = new NamespacedKey(plugin, "spell");
       bindDestroysKey = new NamespacedKey(plugin, "spell.destroy");
       notDroppableKey = new NamespacedKey(plugin, "not-droppable");
   }

}
