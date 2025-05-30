package fr.jamailun.ultimatespellsystem;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Exposes keys.
 */
public final class UssKeys {

    /**
     * Set to a STRING. ID of the spell to bind.
     */
    private static @Getter NamespacedKey legacyBindKey;

    /**
     * Set to a BOOLEAN. If true, item destroyed on use.
     */
    private static @Getter NamespacedKey legacyBindDestroysKey;

    /**
     * Modern bind-data key. Custom type.
     */
    private static @Getter NamespacedKey bindDataKeyV1;

    /**
     * Modern-er bind-data key. Custom type.
     */
    private static @Getter NamespacedKey bindDataKeyV2;

    /**
     * Set to BOOLEAN. If true, object cannot be dropped on death.
     */
    private static @Getter NamespacedKey notDroppableKey;

    private static Plugin plugin;
    static void initialize(@NotNull Plugin plugin) {
        UssKeys.plugin = plugin;
        legacyBindKey = new NamespacedKey(plugin, "spell");
        legacyBindDestroysKey = new NamespacedKey(plugin, "spell.destroy");
        notDroppableKey = new NamespacedKey(plugin, "not-droppable");
        bindDataKeyV1 = new NamespacedKey(plugin, "bind");
        bindDataKeyV2 = new NamespacedKey(plugin, "bindV2");
    }

    @Contract("-> new")
    public static @NotNull NamespacedKey random() {
       return new NamespacedKey(plugin, UUID.randomUUID().toString());
    }

    private static final Map<String, NamespacedKey> KEYS = new HashMap<>();
    public static @NotNull NamespacedKey custom(@NotNull String name) {
        return KEYS.computeIfAbsent(name, n-> new NamespacedKey(plugin, n));
    }

}
