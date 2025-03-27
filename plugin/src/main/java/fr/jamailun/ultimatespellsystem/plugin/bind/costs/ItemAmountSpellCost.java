package fr.jamailun.ultimatespellsystem.plugin.bind.costs;

import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * A cost in health for the item.
 */
@Getter
@Setter
public class ItemAmountSpellCost implements SpellCost {

    private int cost = 1;

    @Override
    public boolean canPay(@NotNull SpellEntity caster) {
        if(cost <= 0) return true;

        if(caster.getBukkitEntity().orElse(null) instanceof LivingEntity le) {
            ItemStack item = le.getActiveItem();
            return item.getAmount() >= cost;
        }
        return false;
    }

    @Override
    public void pay(@NotNull SpellEntity caster) {
        if(caster.getBukkitEntity().orElse(null) instanceof LivingEntity le) {
            ItemStack item = le.getActiveItem();
            item.setAmount(item.getAmount() - cost);
        }
    }
}
