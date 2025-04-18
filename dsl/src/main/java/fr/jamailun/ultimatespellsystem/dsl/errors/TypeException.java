package fr.jamailun.ultimatespellsystem.dsl.errors;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

/**
 * Bad type in a spell.
 */
public class TypeException extends UssException {

    /**
     * Unexpected expression type.
     * @param expression expression source of tbe error. <b>Must have a type set.</b>
     * @param expected expected type instead.
     */
    public TypeException(ExpressionNode expression, TypePrimitive expected) {
        super(expression.firstTokenPosition(), "Expression " + expression + " has type " + expression.getExpressionType() + ", expected " + expected);
    }

    /**
     * Unexpected expression type.
     * @param expression expression source of tbe exception. <b>Must have a type set.</b>
     * @param message exception details.
     */
    public TypeException(ExpressionNode expression, String message) {
        super(expression.firstTokenPosition(), "Expression " + expression + " of type " + expression.getExpressionType() + " : " + message);
    }

    /**
     * Bad token type.
     * @param pos position of tbe token.
     * @param message exception details.
     */
    public TypeException(TokenPosition pos, String message) {
        super(pos, message);
    }

}
