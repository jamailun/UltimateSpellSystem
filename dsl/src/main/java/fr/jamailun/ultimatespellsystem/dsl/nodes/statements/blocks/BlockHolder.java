package fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks;

import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.BlockStatement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Wraps another statement.
 */
public abstract class BlockHolder extends StatementNode {

    protected final StatementNode child;

    /**
     * New instance.
     * @param child child "block".
     */
    protected BlockHolder(@NotNull StatementNode child) {
        if(child instanceof BlockStatement || child instanceof BlockHolder)
            this.child = child;
        else
            this.child = new BlockStatement(List.of(child));
    }

    /**
     * Get the child of the block statement.
     * @return the child statement, often a block.
     */
    public final @NotNull StatementNode getChild() {
        return child;
    }
}
