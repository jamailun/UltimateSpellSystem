package fr.jamailun.ultimatespellsystem.bukkit.runner.builder;

import fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.AllEntitiesAroundExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.PositionOfExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.BiOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.MonoOperator;
import fr.jamailun.ultimatespellsystem.dsl.registries.CustomExpression;
import fr.jamailun.ultimatespellsystem.dsl.registries.CustomExpressionsRegistry;
import fr.jamailun.ultimatespellsystem.dsl.registries.RegistryException;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.expressions.*;

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
    public void handleCustomExpression(CustomExpression expression) {
        // Find the executor of the custom function
        CustomExpressionsRegistry.CustomExpressionProvider provider = CustomExpressionsRegistry.find(expression.getLabel());
        if(provider == null)
            throw new RegistryException(expression.firstTokenPosition(), expression.getLabel());

        // Handle all arguments


        //TODO
        System.err.println("Un-handled custom expression.");
    }

    @Override
    public void handleArray(ArrayExpression expression) {
        List<RuntimeExpression> elements = expression.getElements()
                .stream()
                .map(this::evaluate)
                .toList();
        add(new ArrayNode(elements));
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
        add(new EntityTypeLiteral(literal));
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
    public void handleLocationLiteral(LocationLiteral literal) {
        RuntimeExpression world = evaluate(literal.getWorld());
        RuntimeExpression x = evaluate(literal.getVectorX());
        RuntimeExpression y = evaluate(literal.getVectorY());
        RuntimeExpression z = evaluate(literal.getVectorZ());
        RuntimeExpression yaw = null, pitch = null;
        if(literal.asYawAndPitch()) {
            yaw = evaluate(literal.getYaw());
            pitch = evaluate(literal.getPitch());
        }
        add(new LocationNode(world, x, y, z, yaw, pitch));
    }

    @Override
    public void handleBiOperator(BiOperator operator) {
        RuntimeExpression left = evaluate(operator.getLeft());
        RuntimeExpression right = evaluate(operator.getRight());
        add(switch (operator.getType()) {
            case ADD -> new RunAddOpe(left, right);
            case SUB -> new RunSubOpe(left, right);
            case MUL -> new RunMulDivOpe(left, right, true);
            case DIV -> new RunMulDivOpe(left, right, false);
            case EQUAL -> new RunEqualsOrNotOpe(left, right, true);
            case NOT_EQUAL -> new RunEqualsOrNotOpe(left, right, false);
            case GREATER_OR_EQ -> new RunCompOpe(left, right, true, true);
            case GREATER -> new RunCompOpe(left, right, false, true);
            case LESSER_OR_EQ -> new RunCompOpe(left, right, true, false);
            case LESSER -> new RunCompOpe(left, right, false, false);
            case AND ->  new RunAndOrOpe(left, right, true);
            case OR -> new RunAndOrOpe(left, right, false);
        });
    }

    @Override
    public void handleMonoOperator(MonoOperator operator) {
        //TODO handleMonoOperator
    }

    @Override
    public void handleParenthesis(ParenthesisExpression parenthesis) {
        parenthesis.getExpression().visit(this);
    }

    @Override
    public void handleArrayGet(ArrayGetterExpression arrayGetter) {
        RuntimeExpression array = evaluate(arrayGetter.getArray());
        RuntimeExpression index = evaluate(arrayGetter.getIndex());
        add(new ArrayGetNode(array, index));
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
