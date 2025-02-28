package fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class VariableDefinition {

    private final List<VariableReference> references = new ArrayList<>();
    private final String name;

    private transient Type computedType;

    public void register(@NotNull VariableReference reference) {
        System.out.println("register variable reference : <" + name + "> " + reference);
        references.add(reference);
        computedType = null;
    }

    public @NotNull Type getType(@NotNull TypesContext context) {
        if(computedType != null) return computedType;

        // We need ot compute the type
        for(VariableReference reference : references) {
            Type type = reference.getType(context);
            if(type.is(TypePrimitive.NULL)) {
                // Nothing here
                continue;
            }
            if(computedType == null) {
                computedType = type;
            } else if( ! computedType.equals(type)) {
                throw reference.exception("Cannot change type of an already defined variable (%"+name+"). Previous type: " + computedType + ", new type: " + type + ".");
            }
        }

        // If type not set, then it's a NULL.
        return Objects.requireNonNullElseGet(computedType, TypePrimitive.NULL::asType);
    }

}
