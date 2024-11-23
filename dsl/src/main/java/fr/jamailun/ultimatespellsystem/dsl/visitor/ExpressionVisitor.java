package fr.jamailun.ultimatespellsystem.dsl.visitor;

import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.compute.AllEntitiesAroundExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionCallExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.compute.PositionOfExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.compute.SizeOfExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.BiOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.MonoOperator;
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
    void handleEntityTypeLiteral(@NotNull EntityTypeExpression literal);
    void handleRuntimeLiteral(@NotNull RuntimeLiteral literal);
    void handleDurationLiteral(@NotNull DurationExpression literal);
    void handleEffectLiteral(@NotNull EffectTypeExpression literal);
    void handleLocationLiteral(@NotNull LocationLiteral literal);

    // Operators-ish
    void handleBiOperator(@NotNull BiOperator operator);
    void handleMonoOperator(@NotNull MonoOperator operator);
    void handleParenthesis(@NotNull ParenthesisExpression parenthesis);
    void handleArrayGet(@NotNull ArrayGetterExpression arrayGetter);

    // Specifics
    void handlePropertiesSet(@NotNull PropertiesExpression expression);
    void handleArray(@NotNull ArrayExpression expression);
    void handleVariable(@NotNull VariableExpression expression);

    // Functions
    void handleAllAround(@NotNull AllEntitiesAroundExpression expression);
    void handlePositionOf(@NotNull PositionOfExpression expression);
    void handleSizeOf(@NotNull SizeOfExpression expression);
    void handleFunction(@NotNull FunctionCallExpression expression);

}
