package fr.jamailun.ultimatespellsystem.plugin.bind.costs;

import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 * A cost in {@link HumanEntity} food level.
 */
@Getter
@Setter
public class FoodLevelSpellCost implements SpellCost {

    private int cost;

    @Override
    public boolean canPay(@NotNull SpellEntity caster) {
        if(cost <= 0) return true;

        if(caster.getBukkitEntity().orElse(null) instanceof HumanEntity human) {
            return human.getFoodLevel() >= cost;
        }
        return false;
    }

    @Override
    public void pay(@NotNull SpellEntity caster) {
        if(caster.getBukkitEntity().orElse(null) instanceof HumanEntity human) {
            human.setFoodLevel(human.getFoodLevel() - cost);
        }
    }
}
