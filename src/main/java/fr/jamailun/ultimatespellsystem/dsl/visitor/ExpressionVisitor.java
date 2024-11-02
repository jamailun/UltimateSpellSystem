package fr.jamailun.ultimatespellsystem.dsl.visitor;

import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.AllEntitiesAroundExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.PositionOfExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.BiOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.MonoOperator;
import fr.jamailun.ultimatespellsystem.dsl.registries.CustomExpression;

/**
 * A visitor. Can interact with the node-tree.
 */
public interface ExpressionVisitor {

    // Literals
    void handleNullLiteral(NullExpression literal);
    void handleBooleanLiteral(BooleanExpression literal);
    void handleNumberLiteral(NumberExpression literal);
    void handleStringLiteral(StringExpression literal);
    void handleEntityTypeLiteral(EntityTypeExpression literal);
    void handleRuntimeLiteral(RuntimeLiteral literal);
    void handleDurationLiteral(DurationExpression literal);
    void handleEffectLiteral(EffectTypeExpression literal);
    void handleLocationLiteral(LocationLiteral literal);

    // Operators-ish
    void handleBiOperator(BiOperator operator);
    void handleMonoOperator(MonoOperator operator);
    void handleParenthesis(ParenthesisExpression parenthesis);
    void handleArrayGet(ArrayGetterExpression arrayGetter);

    // Specifics
    void handlePropertiesSet(PropertiesExpression expression);
    void handleArray(ArrayExpression expression);
    void handleVariable(VariableExpression expression);

    // Functions
    void handleAllAround(AllEntitiesAroundExpression expression);
    void handlePositionOf(PositionOfExpression expression);
    void handleCustomExpression(CustomExpression expression);

}
