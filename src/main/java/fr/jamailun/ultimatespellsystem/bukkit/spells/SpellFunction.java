package fr.jamailun.ultimatespellsystem.bukkit.spells;

import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;

import java.util.List;

/**
 * Provides access to java-code function from the spell DSL.
 */
public interface SpellFunction {

    /**
     * Cast this spell function.
     * @param runtime current spellRuntime, provides access to variables.
     */
    void call(SpellRuntime runtime, List<Object> arguments);

}
