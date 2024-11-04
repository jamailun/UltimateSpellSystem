package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class StopNode extends RuntimeStatement {

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        runtime.stop();
    }

    @Override
    public String toString() {
        return "STOP";
    }
}
