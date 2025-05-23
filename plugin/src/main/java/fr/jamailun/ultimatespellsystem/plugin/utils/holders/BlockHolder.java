package fr.jamailun.ultimatespellsystem.plugin.utils.holders;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Holds a block effect.
 */
public class BlockHolder {

    private final Material type;
    private final Duration duration;

    public BlockHolder(@NotNull Material type, @NotNull Duration duration) {
        this.type = type;
        this.duration = duration;
        UssLogger.logDebug("New block-holder : (" + this +")");
    }

    /**
     * Build a Block-holder from a map.
     * @param context the debug location of the attributes, used for printing-purpose.
     * @param values the map of attributes. Expected keys: {type, volume, pitch}
     * @return null if an error occurred.
     */
    public static @Nullable BlockHolder build(@NotNull String context, @NotNull Map<?, ?> values) {
        // Type
        Object typeRaw = values.get("type");
        if(!(typeRaw instanceof String type)) {
            UssLogger.logError("(" + context + ") Invalid material-type : '" + typeRaw + "'.");
            return null;
        }
        Material material;
        try {
            material = Material.valueOf(type.toUpperCase());
        } catch(IllegalArgumentException e) {
            UssLogger.logError("(" + context + ") Unknown material-type : '" + type + "' ("+e.getMessage()+")");
            return null;
        }
        // Count
        Duration duration = new Duration(1, TimeUnit.SECONDS);
        if(values.containsKey("duration")) {
            Object raw = values.get("duration");
            if(!(raw instanceof Duration dur)) {
                UssLogger.logError("(" + context + ") Invalid block-effect duration : '" + raw + "'.");
                return null;
            }
            duration = dur;
        }

        // Create
        return new BlockHolder(material, duration);
    }

    /**
     * Play the block-effect to a location.
     * @param location the non-null location to use.
     */
    public void apply(@NotNull Location location) {
        BlockData newData = type.createBlockData();
        BlockData originalData = location.getBlock().getBlockData();
        List<Player> players = location.getWorld().getPlayers();

        // Send new data
        players.forEach(p -> p.sendBlockChange(location, newData));

        // Reset block data
        UltimateSpellSystem.getScheduler().runTaskLater(() -> players.forEach(p -> p.sendBlockChange(location, originalData)), duration.toTicks());
    }

}
