package fr.jamailun.ultimatespellsystem.dsl2.visitor;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators.*;
import org.jetbrains.annotations.NotNull;

/**
 * A visitor. Can interact with the node-tree.
 */
public interface ExpressionVisitor {

    // Literals
    void handleNullLiteral(@NotNull NullLiteral literal);
    void handleBooleanLiteral(@NotNull BooleanLiteral literal);
    void handleNumberLiteral(@NotNull NumberLiteral literal);
    void handleStringLiteral(@NotNull StringLiteral literal);
    void handleMapLiteral(@NotNull MapLiteral literal);
    void handleDurationLiteral(@NotNull DurationLiteral literal);
    void handleLocationLiteral(@NotNull LocationLiteral literal);

    // Operators-ish
    void handleBiOperator(@NotNull BiOperator operator);
    void handleMonoOperator(@NotNull MonoOperator operator);
    void handleParenthesis(@NotNull ParenthesisExpression parenthesis);
    void handleArrayGet(@NotNull ArrayGetterExpression arrayGetter);
    void handleFieldGet(@NotNull FieldGetExpression fieldGetter);
    void handleFunctionCall(@NotNull FunctionCallExpression functionCall);
    void handleIncrementDecrement(@NotNull IncrementExpression expression);

    // Specifics
    void handleArray(@NotNull ArrayLiteral expression);
    void handleVariable(@NotNull ReferenceExpression expression);

}
