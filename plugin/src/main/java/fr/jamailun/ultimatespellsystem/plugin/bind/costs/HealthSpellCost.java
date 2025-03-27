package fr.jamailun.ultimatespellsystem.plugin.bind.costs;

import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

/**
 * A cost in {@link LivingEntity} health.
 */
@Getter
@Setter
public class HealthSpellCost implements SpellCost {

    private double cost;

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
}
