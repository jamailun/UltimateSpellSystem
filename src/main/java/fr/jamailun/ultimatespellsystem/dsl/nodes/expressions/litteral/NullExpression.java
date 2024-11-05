package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

public class NullExpression extends LiteralExpression<Void> {

    public NullExpression(TokenPosition pos) {
        super(pos);
    }

    @Override
    public Void getRaw() {return null;}

    @Override
    public @NotNull Type getExpressionType() {
        return TypePrimitive.NULL.asType();
    }

    @Override
    public String toString() {
        return "NULL";
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleNullLiteral(this);
    }
}
