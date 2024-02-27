package fr.jamailun.ultimatespellsystem.dsl.visitor;

import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.BiOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.MonoOperator;

/**
 * A visitor. Can interact with the node-tree.
 */
public interface ExpressionVisitor {

    void handleNullLiteral(NullExpression literal);
    void handleBooleanLiteral(BooleanExpression literal);
    void handleNumberLiteral(NumberExpression literal);
    void handleStringLiteral(StringExpression literal);
    void handleEntityTypeLiteral(EntityTypeExpression literal);
    void handleRuntimeLiteral(RuntimeLiteral literal);
    void handleDurationLiteral(DurationExpression literal);
    void handleEffectLiteral(EffectTypeExpression literal);

    void handleBiOperator(BiOperator operator);
    void handleMonoOperator(MonoOperator operator);
    void handleParenthesis(ParenthesisExpression parenthesis);

    void handlePropertiesSet(PropertiesExpression expression);
    void handleAllAround(AllEntitiesAround expression);
    void handleArrayConcat(ArrayConcatExpression expression);
    void handleVariable(VariableExpression expression);


}
