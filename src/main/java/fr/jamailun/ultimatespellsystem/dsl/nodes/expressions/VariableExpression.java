package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;

public class VariableExpression extends ExpressionNode {

    private final String varName;
    private Type varType;

    public VariableExpression(Token token, Type varType) {
        super(token.pos());
        this.varName = token.getContentString();
        this.varType = varType;
    }

    public VariableExpression(Token token) {
        this(token, null);
    }

    public String getVariableName() {
        return varName;
    }

    @Override
    public Type getExpressionType() {
        return varType;
    }

    @Override
    public void validateTypes(TypesContext context) {
        if(varType != null) {
            Type definedType = context.findVariable(varName);
            if(!varType.equals(definedType)) {
                throw new TypeException(this, definedType);
            }
            return;
        }

        varType = context.findVariable(varName);
        if(varType == null)
            throw new SyntaxException(firstTokenPosition(), "Undefined variable '" + varName + "'.");
    }

    @Override
    public String toString() {
        return "%" + varName + "(" + varType + ")";
    }
}
