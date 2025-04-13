package fr.jamailun.ultimatespellsystem.plugin.bind.costs;

import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * No cost.
 */
@Getter @Setter
@NoArgsConstructor
public class NoneSpellCost implements SpellCost {

    @Override
    public boolean canPay(@NotNull SpellEntity ignored) {
        return true;
    }

    @Override
    public void pay(@NotNull SpellEntity ignored) {
        // Do nothing
    }

    @Override
    public @NotNull String serialize() {
        return "none";
    }

}
