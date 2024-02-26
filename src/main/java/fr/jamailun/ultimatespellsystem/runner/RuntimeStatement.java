package fr.jamailun.ultimatespellsystem.runner;

import fr.jamailun.ultimatespellsystem.UltimateSpellSystem;

public abstract class RuntimeStatement {

    public abstract void run(SpellRuntime runtime);

    protected void debug(String message) {
        UltimateSpellSystem.logDebug("SPELL | " + message);
    }

}
