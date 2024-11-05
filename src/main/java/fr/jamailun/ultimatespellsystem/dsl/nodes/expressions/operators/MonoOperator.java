package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

public abstract class MonoOperator extends ExpressionNode {

    protected final ExpressionNode child;

    protected MonoOperator(TokenPosition position, ExpressionNode child) {
        super(position);
        this.child = child;
    }

    public abstract MonoOpeType getType();

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleMonoOperator(this);
    }

    @Override
    public final void validateTypes(TypesContext context) {
        child.validateTypes(context);

       validateTypes(child.getExpressionType());
    }

    public ExpressionNode getChild() {
        return child;
    }

    public abstract void validateTypes(Type childType);

    public enum MonoOpeType {
        NOT,
        SIN,
        COS,
        TAN
    }

}
