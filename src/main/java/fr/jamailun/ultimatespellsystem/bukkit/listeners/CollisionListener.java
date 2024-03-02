package fr.jamailun.ultimatespellsystem.bukkit.listeners;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;

public class CollisionListener implements Listener {

    public CollisionListener(UltimateSpellSystem plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void entityCollides(AreaEffectCloudApplyEvent e) {
        for(LivingEntity en : e.getAffectedEntities()) {
            en.sendMessage("§aCOLLIDES !");
        }
    }

}
