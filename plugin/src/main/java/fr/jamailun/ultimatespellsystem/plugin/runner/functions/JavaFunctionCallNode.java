package fr.jamailun.ultimatespellsystem.plugin.runner.functions;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.functions.RunnableJavaFunction;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.plugin.entities.BukkitSpellEntity;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * A Node that call some java.
 */
@RequiredArgsConstructor
public class JavaFunctionCallNode extends RuntimeExpression {

    private final RunnableJavaFunction function;
    private final List<RuntimeExpression> arguments;

    @Override
    public Object evaluate(@NotNull SpellRuntime runtime) {
        Object value = function.compute(arguments, runtime);

        // Special case for entities. We want to map them as spell entities !
        if(function.getDslDefinition().returnedType().is(TypePrimitive.ENTITY)) {
            if(value instanceof Collection<?> coll) {
                if( ! function.getDslDefinition().returnedType().isCollection()) {
                    throw new RuntimeException("In execution of function '" + function.getDslDefinition().id() + "', return type is " + function.getDslDefinition().returnedType() + ". But got an unexpected COLLECTION " + value);
                }
                return coll.stream()
                        .map(this::mapEntity)
                        .toList();
            }
            return mapEntity(value);
        }

        return value;
    }

    private @Nullable SpellEntity mapEntity(@Nullable Object value) {
        return switch (value) {
            // Null ? We handle it.
            case null -> null;

            // Already a spell-entity.
            case SpellEntity se -> se;

            // Bukkit entity : we wrap it.
            case Entity bukkitEntity -> new BukkitSpellEntity(bukkitEntity);

            default -> throw new RuntimeException("In execution of function '" + function.getDslDefinition().id() + "', return type is " + function.getDslDefinition().returnedType() + ". But got: " + value + " of type" + value.getClass());
        };
    }

    @Override
    @Contract(pure = true)
    public @NotNull String toString() {
        return "CALL[" + function.getId() + "(" + arguments + ")]";
    }
}
