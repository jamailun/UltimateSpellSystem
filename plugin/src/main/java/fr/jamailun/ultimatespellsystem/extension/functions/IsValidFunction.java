package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.errors.InvalidTypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Test if an entity is valid.
 */
public class IsValidFunction extends AbstractFunction {

    public IsValidFunction() {
        super(
                "is_valid",
                // Returns a boolean
                TypePrimitive.BOOLEAN.asType(),
                // Args : the entity tio check.
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "entity", false
                        )
                )
        );
    }

    @Override
    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        Object object = arguments.getFirst().evaluate(runtime);
        return switch (object) {
            case null -> false;
            case Entity entity -> entity.isValid();
            case SpellEntity spellEntity -> spellEntity.isValid();
            default -> throw new InvalidTypeException("is_valid(entity)", "entity", object);
        };
    }
}
