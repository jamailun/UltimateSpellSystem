package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code NULL} representation.
 */
public class NullLiteral extends LiteralExpression<Void> {

    /**
     * New instance.
     * @param pos position of the keyword.
     */
    public NullLiteral(@NotNull TokenPosition pos) {
        super(pos);
    }

    @Override
    public Void getRaw() {return null;}

    @Override
    public @NotNull Type getExpressionType() {
        return Type.NULL;
    }

    @Override
    public String toString() {
        return PREFIX + "NULL" + SUFFIX;
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleNullLiteral(this);
    }
}
