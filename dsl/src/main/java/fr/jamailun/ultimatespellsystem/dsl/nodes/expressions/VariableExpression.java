package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.VariableDefinition;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Variable reference expression.
 */
public class VariableExpression extends ExpressionNode {

    private final String varName;
    private Type runtimeType;

    /**
     * Create a variable expression, which is a REFERENCE to something else.
     * The reference will be obtained during type validation.
     * @param token the token of the variable itself.
     */
    public VariableExpression(@NotNull Token token) {
        super(token.pos());
        this.varName = token.getContentString();
    }

    /**
     * Get the name of the variable.
     * @return a non-null string.
     */
    public @NotNull String getVariableName() {
        return varName;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return runtimeType;
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        VariableDefinition var = context.findVariable(varName);
        if(var == null)
            throw new SyntaxException(firstTokenPosition(), "Undefined variable '" + varName + "'.");
        runtimeType = var.getType(context);
    }

    @Override
    public String toString() {
        return "%" + varName;
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleVariable(this);
    }
}
