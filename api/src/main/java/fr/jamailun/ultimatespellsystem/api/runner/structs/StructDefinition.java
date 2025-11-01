package fr.jamailun.ultimatespellsystem.api.runner.structs;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A structure definition.
 * @param <S> the underlying type of the structure.
 */
public interface StructDefinition<S> {

    /**
     * Get the struct name.
     * @return the non-null name.
     */
    @NotNull String getName();

    /**
     * Get the DSL type matching the value.
     * @return the type equivalence.
     */
    default @NotNull Type asType() {
        return Type.of(getName());
    }

    /**
     * Instantiate the value.
     * @param value value to wrap.
     * @return a non-null instance.
     */
    @NotNull Struct instantiate(S value);

    /**
     * Register a new field on the struct.
     * @param name the name of the field. If not unique, an exception will be thrown.
     * @param type type of the field.
     * @param getter the getter of the field.
     * @param setter optional setter for the field.
     * @param <T> type of the field.
     */
    <T> void registerField(@NotNull String name, @NotNull Type type, @NotNull Function<S, T> getter, @Nullable BiConsumer<S, T> setter);

    /**
     * Register a new function on the struct.
     * @param functionDefinition the DSL function definition.
     * @param function the function that handles the runtime.
     * @param <T> the output type of the function.
     */
    <T> void registerFunction(@NotNull FunctionDefinition functionDefinition, @NotNull BiFunction<S, List<?>, T> function);

}
