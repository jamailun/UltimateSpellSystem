package fr.jamailun.ultimatespellsystem.dsl.errors;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.VariableExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

public class TypeException extends RuntimeException {

    public TypeException(ExpressionNode expression, TypePrimitive expected) {
        super("Expression " + expression + " at " + expression.firstTokenPosition() + " has type " + expression.getExpressionType() + ", expected " + expected);
    }

    public TypeException(ExpressionNode expression, TypePrimitive expected, boolean collection) {
        this(expression, new Type(expected, collection));
    }

    public TypeException(ExpressionNode expression, Type expected) {
        super("Expression " + expression + " at " + expression.firstTokenPosition() + " has type " + expression.getExpressionType() + ", expected " + expected);
    }

    public TypeException(VariableExpression variable, Type got) {
        super("Variable " + variable.getVariableName() + " at " + variable.firstTokenPosition() + " has type " + got + " instead of " + variable.getExpressionType());
    }


    public TypeException(ExpressionNode expression, String message) {
        super("Expression " + expression + " at " + expression.firstTokenPosition() + ": has type " + expression.getExpressionType() + ", " + message);
    }

}
