package fr.jamailun.ultimatespellsystem.bukkit.utils;

import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import org.jetbrains.annotations.NotNull;

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
            string = string.replace("%" + varName, String.valueOf(runtime.variables().get(varName)));
        }
        return string;
    }

}
