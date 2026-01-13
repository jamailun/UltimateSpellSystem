package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.objects;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl2.library.StructDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.api.runner.structs.Struct;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Getter on a field of an object.
 */
@RequiredArgsConstructor
public class StructFieldGetNode extends RuntimeExpression {

    private final TokenPosition pos;
    private final RuntimeExpression object;
    private final StructDefinition objectStruct;
    private final String fieldName;

    @Override
    public @Nullable Object evaluate(@NotNull SpellRuntime runtime) {
        Object target = object.evaluate(runtime);

        // "definition → instance", à partir de OBJECT
        Struct struct = runtime.getStructOf(objectStruct.getName(), target);
        if(struct == null)
            throw new RuntimeException("Cannot get struct '" + objectStruct.getName() + "' from value " + target + ".");

        return struct.getField(pos, fieldName);
    }

    @Override
    public @NotNull String toString() {
        return object + "." + fieldName;
    }
}
