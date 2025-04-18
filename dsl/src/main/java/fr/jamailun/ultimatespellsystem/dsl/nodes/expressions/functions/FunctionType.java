package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A lax-collection type for a function.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FunctionType {

    private final List<TypePrimitive> primitives;
    private final CollectionFilter collectionFilter;

    /**
     * Create a new function-type, that accepts a restricted amount of type primitives.
     * @param types varargs of allowed types.
     * @return a new instance.
     */
    public static @NotNull FunctionType accept(@NotNull TypePrimitive... types) {
        if(types.length == 0) throw new IllegalArgumentException("Cannot have zero primitive.");
        return new FunctionType(List.of(types), CollectionFilter.ANY);
    }

    /**
     * Create a new function-type, that accepts a restricted amount of type primitives <b>as non-collection</b>.
     * @param types varargs of allowed types.
     * @return a new instance.
     */
    public static @NotNull FunctionType acceptOnlyMono(@NotNull TypePrimitive... types) {
        if(types.length == 0) throw new IllegalArgumentException("Cannot have zero primitive.");
        return new FunctionType(List.of(types), CollectionFilter.MONO_ELEMENT);
    }

    /**
     * Create a new function-type, that accepts a restricted amount of type primitives <b>as collection</b>.
     * @param types varargs of allowed types.
     * @return a new instance.
     */
    public static @NotNull FunctionType acceptOnlyCollection(@NotNull TypePrimitive ... types) {
        if(types.length == 0) throw new IllegalArgumentException("Cannot have zero primitive.");
        return new FunctionType(List.of(types), CollectionFilter.LIST);
    }

    /**
     * Compute a strict {@link Type} correspondance.
     * @return a non-null, new Type instance.
     */
    public @NotNull Type asType() {
        return primitives.getFirst().asType(collectionFilter == CollectionFilter.LIST);
    }


    public @NotNull TestResult accepts(@NotNull Type type) {
        if( ! primitives.contains(type.primitive())) {
            return TestResult.error("expected one of " + primitives + ", got " + type.primitive() + ".");
        }

        if( ! collectionFilter.isValid(type)) {
            if(collectionFilter == CollectionFilter.LIST)
                return TestResult.error("expected a LIST, got " + type + ".");
            return TestResult.error("did not expect a LIST, got " + type + ".");
        }

        return TestResult.success();
    }

    @Getter @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class TestResult {
        private final String error;
        public static @NotNull TestResult success() {
            return new TestResult(null);
        }
        public static @NotNull TestResult error(@NotNull String error) {
            return new TestResult(error);
        }
        public boolean isError() {
            return error != null;
        }
        public boolean isSuccess() {
            return error == null;
        }
    }

}
