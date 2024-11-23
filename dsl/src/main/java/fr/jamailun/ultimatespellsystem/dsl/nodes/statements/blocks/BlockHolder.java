package fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks;

import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.BlockStatement;

import java.util.List;

/**
 * Wraps another statement.
 */
public abstract class BlockHolder extends StatementNode {

    protected final StatementNode child;

    protected BlockHolder(StatementNode child) {
        if(child instanceof BlockStatement || child instanceof BlockHolder)
            this.child = child;
        else
            this.child = new BlockStatement(List.of(child));
    }

    public final StatementNode getChild() {
        return child;
    }
}
