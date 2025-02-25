package fr.jamailun.ultimatespellsystem.dsl.errors;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

public class TypeException extends UssException {

    public TypeException(ExpressionNode expression, TypePrimitive expected) {
        super(expression.firstTokenPosition(), "Expression " + expression + " has type " + expression.getExpressionType() + ", expected " + expected);
    }

    public TypeException(ExpressionNode expression, String message) {
        super(expression.firstTokenPosition(), "Expression " + expression + " of type " + expression.getExpressionType() + " : " + message);
    }

    public TypeException(TokenPosition pos, String message) {
        super(pos, message);
    }

}
