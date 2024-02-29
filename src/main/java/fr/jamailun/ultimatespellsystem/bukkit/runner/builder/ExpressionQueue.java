package fr.jamailun.ultimatespellsystem.bukkit.runner.builder;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.AllEntitiesAroundExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.PositionOfExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.BiOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.MonoOperator;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.expressions.*;
import org.apache.commons.lang3.NotImplementedException;

import java.util.*;

public class ExpressionQueue implements ExpressionVisitor {

    private final Deque<RuntimeExpression> expressions = new ArrayDeque<>();
    private Deque<RuntimeExpression> currentExpressions;

    ExpressionQueue() {
        currentExpressions = expressions;
    }

    RuntimeExpression fetch() {
        return expressions.pop();
    }

    @Override
    public void handlePropertiesSet(PropertiesExpression expression) {
        Map<String, RuntimeExpression> map = new HashMap<>();
        for(Map.Entry<String, ExpressionNode> dslEntry : expression.getExpressions().entrySet()) {
            RuntimeExpression node = evaluate(dslEntry.getValue());
            map.put(dslEntry.getKey(), node);
        }
        add(new PropertiesLiteral(map));
    }

    @Override
    public void handleAllAround(AllEntitiesAroundExpression expression) {
        RuntimeExpression distance = evaluate(expression.getDistance());
        RuntimeExpression scope = evaluate(expression.getEntityType());
        RuntimeExpression source = evaluate(expression.getSource());
        boolean including = expression.isIncluding();
        add(new AllAroundNode(distance, scope, source, including));
    }

    @Override
    public void handlePositionOf(PositionOfExpression expression) {
        RuntimeExpression entity = evaluate(expression.getEntity());
        add(new PositionOfNode(entity, expression.getExpressionType().isCollection()));
    }

    @Override
    public void handleArrayConcat(ArrayConcatExpression expression) {
        //TODO !
        throw new NotImplementedException("Not yet implemented: #handleArrayConcat");
    }

    @Override
    public void handleVariable(VariableExpression expression) {
        add(new VariableNode(expression.getVariableName()));
    }

    @Override
    public void handleNullLiteral(NullExpression literal) {
        add(new NullLiteral());
    }

    @Override
    public void handleBooleanLiteral(BooleanExpression literal) {
        add(new RawLiteral<>(literal));
    }

    @Override
    public void handleNumberLiteral(NumberExpression literal) {
        add(new RawLiteral<>(literal));
    }

    @Override
    public void handleStringLiteral(StringExpression literal) {
        add(new RawLiteral<>(literal));
    }

    @Override
    public void handleEntityTypeLiteral(EntityTypeExpression literal) {
        add(new RawLiteral<>(literal));
    }

    @Override
    public void handleRuntimeLiteral(RuntimeLiteral literal) {
        add(new RawLiteral<>(literal));
    }

    @Override
    public void handleDurationLiteral(DurationExpression literal) {
        add(new RawLiteral<>(literal));
    }

    @Override
    public void handleEffectLiteral(EffectTypeExpression literal) {
        add(new RawLiteral<>(literal));
    }

    @Override
    public void handleBiOperator(BiOperator operator) {
        //TODO handleBiOperator
    }

    @Override
    public void handleMonoOperator(MonoOperator operator) {
        //TODO handleMonoOperator
    }

    @Override
    public void handleParenthesis(ParenthesisExpression parenthesis) {
        //TODO handleParenthesis
    }

    private void add(RuntimeExpression expression) {
        currentExpressions.add(expression);
    }

    private RuntimeExpression evaluate(ExpressionNode dsl) {
        currentExpressions = new ArrayDeque<>();
        dsl.visit(this);
        RuntimeExpression node = currentExpressions.pop();
        currentExpressions = expressions;
        return node;
    }

}
