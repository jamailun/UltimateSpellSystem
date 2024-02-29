package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;

import java.util.List;

public class BlockNodes extends RuntimeStatement {

    private final List<RuntimeStatement> children;

    public BlockNodes(List<RuntimeStatement> children) {
        this.children = children;
    }

    @Override
    public void run(SpellRuntime runtime) {
        children.forEach(c -> c.run(runtime));
    }
}
