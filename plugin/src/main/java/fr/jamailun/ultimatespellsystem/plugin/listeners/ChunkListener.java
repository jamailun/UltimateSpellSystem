package fr.jamailun.ultimatespellsystem.plugin.listeners;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Remove summons from unloaded chunks.
 */
public class ChunkListener implements Listener {

    @EventHandler
    void onChunkUnload(@NotNull ChunkUnloadEvent event) {
        for(Entity entity : event.getChunk().getEntities()) {
            // Do nothing if not a summon.
            UltimateSpellSystem.getSummonsManager().remove(entity.getUniqueId());
        }
    }

    @EventHandler
    void onChunkLoad(@NotNull ChunkLoadEvent event) {
        for(Entity entity : event.getChunk().getEntities()) {
            // Do nothing if not a summon.
            UltimateSpellSystem.getSummonsManager().remove(entity.getUniqueId());
        }
    }

}
