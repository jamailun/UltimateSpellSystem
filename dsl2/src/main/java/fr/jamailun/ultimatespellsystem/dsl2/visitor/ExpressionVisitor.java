package fr.jamailun.ultimatespellsystem.dsl2.visitor;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators.BiOperator;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators.MonoOperator;
import org.jetbrains.annotations.NotNull;

/**
 * A visitor. Can interact with the node-tree.
 */
public interface ExpressionVisitor {

    // Literals
    void handleNullLiteral(@NotNull NullExpression literal);
    void handleBooleanLiteral(@NotNull BooleanExpression literal);
    void handleNumberLiteral(@NotNull NumberExpression literal);
    void handleStringLiteral(@NotNull StringExpression literal);
    void handleMapLiteral(@NotNull MapLiteral literal);
    void handleDurationLiteral(@NotNull DurationExpression literal);
    void handleLocationLiteral(@NotNull LocationLiteral literal);

    // Operators-ish
    void handleBiOperator(@NotNull BiOperator operator);
    void handleMonoOperator(@NotNull MonoOperator operator);
    void handleParenthesis(@NotNull ParenthesisExpression parenthesis);
    void handleArrayGet(@NotNull ArrayGetterExpression arrayGetter);
    void handleFieldGet(@NotNull FieldGetExpression fieldGetter);
    void handleFunctionCall(@NotNull FunctionCallExpression functionCall);

    // Specifics
    void handleArray(@NotNull ArrayLiteral expression);
    void handleVariable(@NotNull ReferenceExpression expression);

}
