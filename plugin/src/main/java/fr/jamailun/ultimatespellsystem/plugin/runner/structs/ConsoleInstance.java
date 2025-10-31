package fr.jamailun.ultimatespellsystem.plugin.runner.structs;

/**
 * Structure for the console.
 */
public class ConsoleInstance extends AbstractStructInstance<Void> {

    /**
     * Create a new console instance.
     */
    public ConsoleInstance() {
        super(ConsoleDefinition.get(), null);
    }

}
