package fr.jamailun.ultimatespellsystem.plugin.runner.structs;

import fr.jamailun.ultimatespellsystem.api.runner.structs.Struct;
import fr.jamailun.ultimatespellsystem.api.runner.structs.StructDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class StructsLibrary {

    private final Map<String, StructDefinition<?>> definitions = new HashMap<>();

    public @Nullable StructDefinition<?> find(@NotNull String name) {
        return definitions.get(name);
    }

    public void register(@NotNull StructDefinition<?> struct) {
        definitions.put(struct.getName(), struct);
    }

    @SuppressWarnings("unchecked")
    public <S> @NotNull Struct instantiate(@NotNull String structName, @NotNull S object) {
        StructDefinition<S> def = (StructDefinition<S>) find(structName);
        if(def == null)
            throw new TypeException(TokenPosition.unknown(), "Could not find a definition for struct name '" + structName + "'.");
        return def.instantiate(object);
    }

}
