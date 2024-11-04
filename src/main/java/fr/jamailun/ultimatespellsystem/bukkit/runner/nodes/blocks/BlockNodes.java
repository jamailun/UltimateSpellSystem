package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.StringJoiner;

public class BlockNodes extends RuntimeStatement {

    private final List<RuntimeStatement> children;

    public BlockNodes(List<RuntimeStatement> children) {
        this.children = children;
    }

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        for(RuntimeStatement child : children) {
            child.run(runtime);
            if(runtime.isStopped())
                return;
        }
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(";\n", "{", "}");
        children.forEach(c -> sj.add(c.toString()));
        return sj.toString();
    }
}
