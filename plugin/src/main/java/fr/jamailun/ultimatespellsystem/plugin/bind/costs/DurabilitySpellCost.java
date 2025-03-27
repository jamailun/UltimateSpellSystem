package fr.jamailun.ultimatespellsystem.plugin.bind.costs;

import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 * A cost in durability for the item.
 */
@Getter
@Setter
public class DurabilitySpellCost implements SpellCost {

    private int cost;

    @Override
    public boolean canPay(@NotNull SpellEntity caster) {
        if(cost <= 0) return true;

        if(caster.getBukkitEntity().orElse(null) instanceof LivingEntity le) {
            ItemStack item = le.getActiveItem();
            return item.getItemMeta() instanceof Damageable;
        }
        return false;
    }

    @Override
    public void pay(@NotNull SpellEntity caster) {
        if(caster.getBukkitEntity().orElse(null) instanceof LivingEntity le) {
            ItemStack item = le.getActiveItem();
            ItemMeta meta = item.getItemMeta();
            if(meta instanceof Damageable damageable) {
                damageable.setDamage(damageable.getDamage() + cost);
                item.setItemMeta(meta);
            }
        }
    }
}
