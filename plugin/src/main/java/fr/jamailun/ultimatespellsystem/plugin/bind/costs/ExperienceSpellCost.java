package fr.jamailun.ultimatespellsystem.plugin.bind.costs;

import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A cost in {@link Player} experience.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ExperienceSpellCost implements SpellCost {

    private int cost = 1;
    private boolean levels = true;

    public ExperienceSpellCost(@NotNull List<String> serialized) {
        this.cost = Integer.parseInt(serialized.getFirst());
        if(serialized.size() > 1)
            this.levels = Boolean.parseBoolean(serialized.getLast());
    }

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

    @Override
    public @NotNull String serialize() {
        return cost + ";" + levels;
    }

    @Override
    public String toString() {
        return "Experience[" + cost + " " + (levels?"levels":"points") + "]";
    }
}
