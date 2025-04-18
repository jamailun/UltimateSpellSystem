package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnknownFunctionException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A ray-cast to find a hit block.
 */
public class CastSpellFunction extends AbstractFunction {

    public CastSpellFunction() {
        super(
                "cast",
                // Returns a boolean
                TypePrimitive.BOOLEAN.asType(),
                // Args :
                // - caster : the caster
                // - spell : the spell ID
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "caster", false
                        ),
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.STRING),
                                "spell", false
                        )
                )
        );
    }

    @Override
    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        SpellEntity entity = toSpellEntity("cast(entity)", arguments.getFirst(), runtime);
        if(entity == null) return false;
        String spellId = runtime.safeEvaluate(arguments.get(1), String.class);

        Spell spell = UltimateSpellSystem.getSpellsManager().getSpell(spellId);
        if(spell == null) {
            throw new UnknownFunctionException("Unknown spell ID: '" + spellId + "'.");
        }

        SpellRuntime newRuntime = runtime.makeChildNewCaster(entity);
        return spell.castNotCancellable(entity, newRuntime);
    }
}
