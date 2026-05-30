package fr.jamailun.ultimatespellsystem.extension.mythicMobs;

import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * When MM is configurer its skills, register the USS handler dynamically.
 */
public class MythicMobConfigurer implements Listener {

  @EventHandler
  void onMechanicLoad(@NotNull MythicMechanicLoadEvent event) {
    if (event.getMechanicName().equalsIgnoreCase("uss_cast")) {
      event.register(
          new MythicMobSkill(
              event.getContainer().getManager(),
              event.getContainer().getFile(),
              event.getContainer().getInternalName(),
              event.getConfig()
          )
      );
    }
  }

}
