package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

public class RuntimeLiteral extends LiteralExpression<String> {

    private final String value;

    public RuntimeLiteral(Token token) {
        super(token.pos());
        this.value = token.getContentString();
    }

    @Override
    public String getRaw() {
        return value;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return TypePrimitive.CUSTOM.asType();
    }

    @Override
    public String toString() {
        return PREFIX + "?'" + value + "'" + SUFFIX;
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleRuntimeLiteral(this);
    }
}
