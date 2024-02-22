package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;

public class VariableExpression extends ExpressionNode {

    private final String varName;
    private Type runtimeType;

    /**
     * Create a variable expression, which is a REFERENCE to something else.
     * The reference will be obtained during type validation.
     * @param token the token of the variable itself.
     */
    public VariableExpression(Token token) {
        super(token.pos());
        this.varName = token.getContentString();
    }

    public String getVariableName() {
        return varName;
    }

    @Override
    public Type getExpressionType() {
        return runtimeType;
    }

    @Override
    public void validateTypes(TypesContext context) {
        TypesContext.VariableDefinition var = context.findVariable(varName);
        if(var == null)
            throw new SyntaxException(firstTokenPosition(), "Undefined variable '" + varName + "'.");
        runtimeType = var.computeType(context);
    }

    @Override
    public String toString() {
        return "%" + varName + "(" + runtimeType + ")";
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
        visitor.handleVariable(this);
    }
}
