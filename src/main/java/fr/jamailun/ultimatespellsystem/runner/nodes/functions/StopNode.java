package fr.jamailun.ultimatespellsystem.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.runner.SpellRuntime;

public class StopNode extends RuntimeStatement {
    @Override
    public void run(SpellRuntime runtime) {
        runtime.stop();
    }
}
