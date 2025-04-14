package fr.jamailun.ultimatespellsystem.plugin.bind.costs;

import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A cost in {@link LivingEntity} health.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class HealthSpellCost implements SpellCost {

    private double cost = 1;

    public HealthSpellCost(@NotNull List<String> serialized) {
        this.cost = Double.parseDouble(serialized.getFirst());
    }

    @Override
    public boolean canPay(@NotNull SpellEntity caster) {
        if(cost <= 0) return true;

        if(caster.getBukkitEntity().orElse(null) instanceof LivingEntity le) {
            return le.getHealth() > cost;
        }
        return false;
    }

    @Override
    public void pay(@NotNull SpellEntity caster) {
        if(caster.getBukkitEntity().orElse(null) instanceof LivingEntity le) {
            le.setHealth(le.getHealth() - cost);
        }
    }

    @Override
    public @NotNull String serialize() {
        return String.valueOf(cost);
    }

    @Override
    public String toString() {
        return "Health[" + cost + "]";
    }
}
