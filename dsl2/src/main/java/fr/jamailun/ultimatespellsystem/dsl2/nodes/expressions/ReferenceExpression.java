package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl2.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.VariableDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * A raw reference expression
 */
public class ReferenceExpression extends ExpressionNode {

    private final String varName;
    private Type runtimeType;

    /**
     * Create a variable expression, which is a REFERENCE to something else.
     * The reference will be obtained during type validation.
     * @param token the token of the variable itself.
     */
    public ReferenceExpression(@NotNull Token token) {
        super(token.pos());
        this.varName = token.getContentString();
    }

    public void signalType(@NotNull Type type) {
        //TODO trucs
        runtimeType = type;
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
