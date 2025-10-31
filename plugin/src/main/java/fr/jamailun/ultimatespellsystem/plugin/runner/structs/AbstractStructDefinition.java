package fr.jamailun.ultimatespellsystem.plugin.runner.structs;

import com.google.common.base.Preconditions;
import fr.jamailun.ultimatespellsystem.dsl2.errors.UnknownFieldException;
import fr.jamailun.ultimatespellsystem.dsl2.errors.UnknownFunctionException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Internal abstract struct definition.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractStructDefinition<S> implements StructDefinition<S> {

    protected static final Type TYPE_LOCATION = Type.of(fr.jamailun.ultimatespellsystem.dsl2.library.StructDefinition.LAZY_TYPE_LOCATION);
    protected static final Type TYPE_ENTITY = Type.of(fr.jamailun.ultimatespellsystem.dsl2.library.StructDefinition.LAZY_TYPE_ENTITY);

    private final Map<String, FieldMetadata<?>> fields = new HashMap<>();
    private final Map<String, FunctionMetadata<?>> functions = new HashMap<>();

    @Getter protected final String structName;

    @Override
    public <T> void registerField(@NotNull String name, @NotNull Type type, @NotNull Function<S, T> getter, @Nullable BiConsumer<S, T> setter) {
        Preconditions.checkState(!fields.containsKey(name), "The field '" + name + " has already be defined in struct " + structName + ".");
        fields.put(name, new FieldMetadata<>(name, type, getter, setter));
        dsl().registerField(name, type);
    }

    @Override
    public <T> void registerFunction(@NotNull FunctionDefinition def, @NotNull BiFunction<S, List<?>, T> impl) {
        String name = def.id();
        Preconditions.checkState(!functions.containsKey(name), "The function '" + name + " has already be defined in struct " + structName + ".");
        functions.put(name, new FunctionMetadata<>(def, impl));
        dsl().registerFunction(def);
    }

    protected void registerNumber(@NotNull String name, @NotNull Function<S, Number> getter) {
        registerField(name, Type.of(TypePrimitive.NUMBER), s -> getter.apply(s).doubleValue(), null);
    }

    protected void registerLocation(@NotNull String name, @NotNull Function<S, Location> getter) {
        registerField(name, TYPE_LOCATION, getter, null);
    }

    protected void registerString(@NotNull String name, @NotNull Function<S, String> getter) {
        registerField(name, Type.of(TypePrimitive.STRING), getter, null);
    }

    protected void registerNumber(@NotNull String name, @NotNull Function<S, Number> getter, @NotNull BiConsumer<S, Number> setter) {
        registerField(name, Type.of(TypePrimitive.NUMBER), s -> getter.apply(s).doubleValue(), setter::accept);
    }

    protected void registerNullFunc(@NotNull String name, @NotNull BiConsumer<S, List<?>> impl, FunctionArgument... args) {
        registerFunction(
                FunctionDefinition.of(name, TypePrimitive.NULL.asType(), args),
                (s, params) -> {
                    impl.accept(s, params);
                    return null;
                }
        );
    }

    /**
     * Get the DSL definition.
     * @return the DSL definition of the object.
     */
    protected abstract @NotNull fr.jamailun.ultimatespellsystem.dsl2.library.StructDefinition dsl();

    /**
     * Get a value.
     * @param object object to get the value from.
     * @param pos token pos
     * @param fieldName name of the field.
     * @param <T> parameter type.
     * @return null if value not found.
     */
    protected <T> @Nullable T get(@NotNull S object, @NotNull TokenPosition pos, @NotNull String fieldName) {
        FieldMetadata<T> field = field(pos, fieldName);
        return field.getter.apply(object);
    }

    /**
     * Set a value.
     * @param object object to set the value of.
     * @param pos token pos
     * @param fieldName field name.
     * @param value value to set.
     * @param <T> parameter type.
     */
    protected <T> void set(@NotNull S object, @NotNull TokenPosition pos, @NotNull String fieldName, @Nullable T value) {
        FieldMetadata<T> field = field(pos, fieldName);
        Preconditions.checkState(field.setter != null, "The field '" + field + "' of " + structName + " is NOT mutable.");
        field.setter.accept(object, value);
    }

    /**
     * Call a function.
     * @param object object to call the function on.
     * @param pos token pos.
     * @param funcName function name.
     * @param params list of parameters passed to the function.
     * @return the output value. May be null.
     * @param <T> function-output parameter type.
     */
    protected <T> @Nullable T call(@NotNull S object, @NotNull TokenPosition pos, @NotNull String funcName, @NotNull List<?> params) {
        FunctionMetadata<T> function = function(pos, funcName);
        return function.implementation.apply(object, params);
    }

    @SuppressWarnings("unchecked")
    private <T> @NotNull FieldMetadata<T> field(@NotNull TokenPosition pos, @NotNull String fieldName) {
        FieldMetadata<T> field = (FieldMetadata<T>) fields.get(fieldName);
        if(field == null)
            throw new UnknownFieldException(pos, structName, fieldName);
        return field;
    }

    @SuppressWarnings("unchecked")
    private <T> @NotNull FunctionMetadata<T> function(@NotNull TokenPosition pos, @NotNull String funcName) {
        FunctionMetadata<T> func = (FunctionMetadata<T>) functions.get(funcName);
        if(func == null)
            throw new UnknownFunctionException(pos, structName, funcName);
        return func;
    }

    /**
     * Meta-data holder for types.
     * @param <T> type of the field.
     */
    @RequiredArgsConstructor
    private class FieldMetadata<T> {
        private final @NotNull String name;
        private final @NotNull Type type;
        private final @NotNull Function<S, T> getter;
        private final @Nullable BiConsumer<S, T> setter;
    }

    @RequiredArgsConstructor
    private class FunctionMetadata<T> {
        private final @NotNull FunctionDefinition definition;
        private final @NotNull BiFunction<S, List<?>, T> implementation;
    }
}
