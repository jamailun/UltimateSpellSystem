package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.visitor.Visitor;

public class StopStatement extends StatementNode {

    @Override
    public void validateTypes(TypesContext context) {}

    @Override
    public void visit(Visitor visitor) {
        visitor.handleStop(this);
    }
}
