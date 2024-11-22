package fr.jamailun.ultimatespellsystem.bukkit.listeners;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bukkit.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.bukkit.UssKeys;
import fr.jamailun.ultimatespellsystem.api.bukkit.providers.SummonPropertiesProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class EntityDeathListener implements Listener {

    @EventHandler
    void entityDies(@NotNull EntityDeathEvent event) {
        // Is the entity a summon ? If so, if non-droppable, will not drop anything.
        SummonAttributes summon = UltimateSpellSystem.getSummonsManager().find(event.getEntity().getUniqueId());
        if(summon != null) {
            if(summon.tryGetAttribute(SummonPropertiesProvider.ATTRIBUTE_MOB_CAN_DROP, Boolean.class, false)) {
                event.getDrops().clear();
                return;
            }
        }

        // Remove drops that cannot be dropped (NBT)
        event.getDrops().removeIf(this::cannotBeDropped);
    }

    private boolean cannotBeDropped(@NotNull ItemStack item) {
        if(item.getItemMeta() == null)
            return false;
        PersistentDataContainer nbt = item.getItemMeta().getPersistentDataContainer();
        Boolean cannotDrop = nbt.get(UssKeys.getNotDroppableKey(), PersistentDataType.BOOLEAN);
        return cannotDrop != null && cannotDrop;
    }

}
