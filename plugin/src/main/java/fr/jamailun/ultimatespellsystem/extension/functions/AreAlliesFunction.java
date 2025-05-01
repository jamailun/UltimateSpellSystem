package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.providers.AlliesProvider;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Test if two entities
 */
public class AreAlliesFunction extends AbstractFunction {

    public AreAlliesFunction() {
        super(
                "are_allies",
                // Returns boolean
                TypePrimitive.BOOLEAN.asType(),
                // Args :
                // - a : entity 1
                // - b : entity 2
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "first-entity", false
                        ),
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "second-entity", false
                        )
                )
        );
    }

    @Override
    public Boolean compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        SpellEntity first = toSpellEntity("are_allies:first-entity", arguments.get(0), runtime);
        SpellEntity second = toSpellEntity("are_allies:second-entity", arguments.get(1), runtime);
        if(first == null || second == null) return false;
        return AlliesProvider.instance().testForAllies(first, second) == AlliesProvider.AlliesResult.ALLIES;
    }
}
