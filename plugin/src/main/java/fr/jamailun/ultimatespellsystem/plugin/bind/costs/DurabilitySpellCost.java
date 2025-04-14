package fr.jamailun.ultimatespellsystem.plugin.bind.costs;

import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A cost in durability for the item.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DurabilitySpellCost implements SpellCost {

    private int cost;

    public DurabilitySpellCost(@NotNull List<String> serialized) {
        this.cost = Integer.parseInt(serialized.getFirst());
    }

    @Override
    public boolean canPay(@NotNull SpellEntity caster) {
        if(cost <= 0) return true;
        if(caster.getBukkitEntity().orElse(null) instanceof HumanEntity human) {
            ItemStack item = human.getInventory().getItemInMainHand();
            return item.getItemMeta() instanceof Damageable;
        }
        return false;
    }

    @Override
    public void pay(@NotNull SpellEntity caster) {
        if(caster.getBukkitEntity().orElse(null) instanceof HumanEntity human) {
            ItemStack item = human.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            if(meta instanceof Damageable damageable) {
                int newDamage = damageable.getDamage() + cost;
                int maxDamage = damageable.hasMaxDamage() ? damageable.getMaxDamage() : item.getType().getMaxDurability();
                if(newDamage > maxDamage) {
                    item.setAmount(item.getAmount() - 1);
                    human.getLocation().getWorld().playSound(human.getLocation(), Sound.ITEM_SHIELD_BREAK, 0.6f, 1f);
                } else {
                    damageable.setDamage(newDamage);
                    item.setItemMeta(meta);
                }
            }
        }
    }

    @Override
    public @NotNull String serialize() {
        return String.valueOf(cost);
    }

    @Override
    public String toString() {
        return "Durability[" + cost + "]";
    }
}
