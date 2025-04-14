package fr.jamailun.ultimatespellsystem.plugin.bind.costs;

import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
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

    @Override
    public @NotNull String serialize() {
        return String.valueOf(cost);
    }

    @Override
    public String toString() {
        return "Durability[" + cost + "]";
    }
}
