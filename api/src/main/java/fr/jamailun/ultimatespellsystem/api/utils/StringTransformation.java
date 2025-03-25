package fr.jamailun.ultimatespellsystem.api.utils;

import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * USS string transformation. For example, will render variables to a string.
 */
public final class StringTransformation {

    /**
     * Apply all transformations to a string.
     * @param string the string to transform.
     * @param runtime the spell runtime context.
     * @return a non-null string.
     */
    public static @NotNull String transformString(@NotNull String string, @NotNull SpellRuntime runtime) {
        // Transform variables
        string = transformVariables(string, runtime);
        // Transform custom
        //TODO tr custom
        //TODO resolve placeholder API transforms??
        return string;
    }

    private static @NotNull String transformVariables(@NotNull String string, @NotNull SpellRuntime runtime) {
        for(String varName : runtime.variables().names()) {
            String value = Objects.requireNonNullElse(String.valueOf(runtime.variables().get(varName)), "null");
            string = string.replace("%" + varName, value);
        }
        return string;
    }

    /**
     * Parse a string with the legacy color code.
     * @param string a non-null string.
     * @return a non-null string with deserialized legacy color codes.
     * @see LegacyComponentSerializer
     */
    public static @NotNull Component parse(@NotNull String string) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
    }

}
