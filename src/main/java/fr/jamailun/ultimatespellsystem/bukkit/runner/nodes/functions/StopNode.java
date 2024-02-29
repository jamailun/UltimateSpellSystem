package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;

public class StopNode extends RuntimeStatement {
    @Override
    public void run(SpellRuntime runtime) {
        runtime.stop();
    }
}
