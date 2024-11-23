package fr.jamailun.ultimatespellsystem.api.utils;

import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class StringTransformation {

    public static @NotNull String transformString(@NotNull String string, @NotNull SpellRuntime runtime) {
        // Transform variables
        string = transformVariables(string, runtime);
        // Transform custom
        //TODO tr custom
        return string;
    }

    private static @NotNull String transformVariables(@NotNull String string, @NotNull SpellRuntime runtime) {
        for(String varName : runtime.variables().names()) {
            String value = Objects.requireNonNullElse(String.valueOf(runtime.variables().get(varName)), "null");
            string = string.replace("%" + varName, value);
        }
        return string;
    }

    public static @NotNull Component parse(@NotNull String string) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
    }

}
