package fr.jamailun.ultimatespellsystem.plugin.bind.costs;

import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Combine multiple spell costs.
 */
@Getter
public class AndSpellCost implements SpellCost {

    private final List<SpellCost> costs = new ArrayList<>();

    @Override
    public boolean canPay(@NotNull SpellEntity caster) {
        return costs.isEmpty() || costs.stream().allMatch(sc -> sc.canPay(caster));
    }

    @Override
    public void pay(@NotNull SpellEntity caster) {
        costs.forEach(sc -> sc.pay(caster));
    }
}
