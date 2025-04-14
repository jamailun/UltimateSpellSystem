package fr.jamailun.ultimatespellsystem.plugin.bind.costs;

import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A cost in {@link HumanEntity} food level.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FoodLevelSpellCost implements SpellCost {

    private int cost = 1;

    public FoodLevelSpellCost(@NotNull List<String> serialized) {
        this.cost = Integer.parseInt(serialized.getFirst());
    }

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

    @Override
    public @NotNull String serialize() {
        return String.valueOf(cost);
    }

    @Override
    public String toString() {
        return "FoodLevel[" + cost + "]";
    }
}
