package fr.jamailun.ultimatespellsystem.plugin.runner.builder;

import fr.jamailun.ultimatespellsystem.api.providers.JavaFunctionProvider;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnknownFunctionException;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.plugin.runner.functions.JavaFunctionCallNode;
import fr.jamailun.ultimatespellsystem.api.runner.functions.RunnableJavaFunction;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.compute.AllEntitiesAroundExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionCallExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.compute.PositionOfExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.compute.SizeOfExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.BiOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.MonoOperator;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.list.ArrayGetNode;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.list.ListAddRemOpe;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.list.ListContainsOpe;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.list.ListRemIndexOpe;
import org.jetbrains.annotations.NotNull;

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
    public void handlePropertiesSet(@NotNull PropertiesExpression expression) {
        Map<String, RuntimeExpression> map = new HashMap<>();
        for(Map.Entry<String, ExpressionNode> dslEntry : expression.getExpressions().entrySet()) {
            RuntimeExpression node = evaluate(dslEntry.getValue());
            map.put(dslEntry.getKey(), node);
        }
        add(new PropertiesLiteral(map));
    }

    @Override
    public void handleAllAround(@NotNull AllEntitiesAroundExpression expression) {
        RuntimeExpression distance = evaluate(expression.getDistance());
        RuntimeExpression scope = evaluate(expression.getEntityType());
        RuntimeExpression source = evaluate(expression.getSource());
        boolean including = expression.isIncluding();
        add(new AllAroundNode(distance, scope, source, including));
    }

    @Override
    public void handlePositionOf(@NotNull PositionOfExpression expression) {
        RuntimeExpression entity = evaluate(expression.getEntity());
        add(new PositionOfNode(entity, expression.getExpressionType().isCollection()));
    }

    @Override
    public void handleSizeOf(@NotNull SizeOfExpression expression) {
        RuntimeExpression child = evaluate(expression.getChild());
        add(new SizeOfNode(child));
    }

    @Override
    public void handleFunction(@NotNull FunctionCallExpression expression) {
        // 1. Find the EXECUTION of the function definition
        RunnableJavaFunction function = JavaFunctionProvider.instance().find(expression.getFunction().id());
        if(function == null)
            throw new UnknownFunctionException(expression.getFunction().id());
        // 2. Map arguments
        List<RuntimeExpression> arguments = expression.getArguments().stream()
                .map(this::evaluate)
                .toList();
        // 3. Create node
        add(new JavaFunctionCallNode(function, arguments));
    }

    @Override
    public void handleArray(@NotNull ArrayExpression expression) {
        List<RuntimeExpression> elements = expression.getElements()
                .stream()
                .map(this::evaluate)
                .toList();
        add(new ArrayNode(elements));
    }

    @Override
    public void handleVariable(@NotNull VariableExpression expression) {
        add(new VariableNode(expression.getVariableName()));
    }

    @Override
    public void handleNullLiteral(@NotNull NullExpression literal) {
        add(new NullLiteral());
    }

    @Override
    public void handleBooleanLiteral(@NotNull BooleanExpression literal) {
        add(new RawLiteral<>(literal));
    }

    @Override
    public void handleNumberLiteral(@NotNull NumberExpression literal) {
        add(new RawLiteral<>(literal));
    }

    @Override
    public void handleStringLiteral(@NotNull StringExpression literal) {
        add(new RawLiteral<>(literal));
    }

    @Override
    public void handleEntityTypeLiteral(@NotNull EntityTypeExpression literal) {
        add(new EntityTypeLiteral(literal));
    }

    @Override
    public void handleRuntimeLiteral(@NotNull RuntimeLiteral literal) {
        add(new RawLiteral<>(literal));
    }

    @Override
    public void handleDurationLiteral(@NotNull DurationExpression literal) {
        add(new RawLiteral<>(literal));
    }

    @Override
    public void handleLocationLiteral(@NotNull LocationLiteral literal) {
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
    public void handleBiOperator(@NotNull BiOperator operator) {
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
            case LIST_ADD -> new ListAddRemOpe(left, right, true);
            case LIST_REM -> new ListAddRemOpe(left, right, false);
            case LIST_CONTAINS -> new ListContainsOpe(left, right);
            case LIST_REM_INDEX -> new ListRemIndexOpe(left, right);
        });
    }

    @Override
    public void handleMonoOperator(@NotNull MonoOperator operator) {
        RuntimeExpression child = evaluate(operator.getChild());
        if(operator.getType() == MonoOperator.MonoOpeType.NOT) {
            add(new RunNotOpe(child));
        } else {
            add(new RunMathOpe(child, operator.getType()));
        }
    }

    @Override
    public void handleParenthesis(@NotNull ParenthesisExpression parenthesis) {
        parenthesis.getExpression().visit(this);
    }

    @Override
    public void handleArrayGet(@NotNull ArrayGetterExpression arrayGetter) {
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
