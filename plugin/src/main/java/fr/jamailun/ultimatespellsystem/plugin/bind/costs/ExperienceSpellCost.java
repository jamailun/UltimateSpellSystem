package fr.jamailun.ultimatespellsystem.plugin.bind.costs;

import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A cost in {@link Player} experience.
 */
@Getter
@Setter
public class ExperienceSpellCost implements SpellCost {

    private int cost = 1;
    private boolean levels = true;

    @Override
    public boolean canPay(@NotNull SpellEntity caster) {
        if(cost <= 0) return true;

        if(caster.getBukkitEntity().orElse(null) instanceof Player player) {
            return (levels ? player.getLevel() : player.getTotalExperience()) >= cost;
        }
        return false;
    }

    @Override
    public void pay(@NotNull SpellEntity caster) {
        if(caster.getBukkitEntity().orElse(null) instanceof Player player) {
            if(levels) {
                player.setLevel(player.getLevel() - cost);
            } else {
                player.setTotalExperience(player.getTotalExperience() - cost);
            }
        }
    }
}
