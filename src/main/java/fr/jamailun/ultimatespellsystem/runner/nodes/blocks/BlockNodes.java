package fr.jamailun.ultimatespellsystem.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.runner.SpellRuntime;

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
