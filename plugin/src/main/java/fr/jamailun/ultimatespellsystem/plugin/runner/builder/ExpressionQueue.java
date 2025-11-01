package fr.jamailun.ultimatespellsystem.plugin.runner.builder;

import fr.jamailun.ultimatespellsystem.api.providers.JavaFunctionProvider;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.functions.RunnableJavaFunction;
import fr.jamailun.ultimatespellsystem.dsl2.library.StructDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.FunctionCallExpression;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.litteral.NullLiteral;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators.IncrementExpression;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.plugin.runner.functions.JavaFunctionCallNode;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators.BiOperator;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators.MonoOperator;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.ExpressionVisitor;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.objects.ArrayGetNode;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.objects.GlobalFunctionCallNode;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.objects.StructFieldGetNode;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.objects.StructFunctionCallNode;
import org.jetbrains.annotations.Contract;
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
    public void handleNullLiteral(@NotNull NullLiteral literal) {
        add(new fr.jamailun.ultimatespellsystem.plugin.runner.nodes.expressions.NullLiteral());
    }

    @Override
    public void handleBooleanLiteral(@NotNull BooleanLiteral literal) {
        add(new RawLiteral<>(literal.getRaw()));
    }

    @Override
    public void handleNumberLiteral(@NotNull NumberLiteral literal) {
        add(new RawLiteral<>(literal.getRaw()));
    }

    @Override
    public void handleStringLiteral(@NotNull StringLiteral literal) {
        add(new RawLiteral<>(literal.getRaw()));
    }

    @Override
    public void handleDurationLiteral(@NotNull DurationLiteral literal) {
        add(new RawLiteral<>(literal.getRaw()));
    }

    @Override
    public void handleMapLiteral(@NotNull MapLiteral literal) {
        Map<String, RuntimeExpression> map = new HashMap<>();
        for(Map.Entry<String, ExpressionNode> dslEntry : literal.getExpressions().entrySet()) {
            RuntimeExpression node = evaluate(dslEntry.getValue());
            map.put(dslEntry.getKey(), node);
        }
        add(new PropertiesLiteral(map));
    }

    @Override
    public void handleArray(@NotNull ArrayLiteral expression) {
        List<RuntimeExpression> elements = expression.getElements()
                .stream()
                .map(this::evaluate)
                .toList();
        add(new ArrayNode(elements));
    }

    @Override
    public void handleVariable(@NotNull ReferenceExpression expression) {
        add(new VariableNode(expression.getVariableName()));
    }

    @Override
    public void handleLocationLiteral(@NotNull LocationLiteral literal) {
        RuntimeExpression world = evaluate(literal.getWorld());
        RuntimeExpression x = evaluate(literal.getVectorX());
        RuntimeExpression y = evaluate(literal.getVectorY());
        RuntimeExpression z = evaluate(literal.getVectorZ());
        RuntimeExpression yaw = null, pitch = null;
        if(literal.hasYawAndPitch()) {
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
        });
    }

    @Override
    public void handleMonoOperator(@NotNull MonoOperator operator) {
        RuntimeExpression child = evaluate(operator.getChild());
        if(operator.getType() == MonoOperator.MonoOpeType.NOT) {
            add(new RunNotOpe(child));
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

    @Override
    public void handleFieldGet(@NotNull FieldGetExpression fieldGetter) {
        RuntimeExpression pointer = evaluate(fieldGetter.getLeftExpression());
        StructDefinition pointerStruct = fieldGetter.getLeftStruct();
        String fieldName = fieldGetter.getFieldName();
        add(new StructFieldGetNode(fieldGetter.firstTokenPosition(), pointer, pointerStruct, fieldName));
    }

    @Override
    public void handleFunctionCall(@NotNull FunctionCallExpression functionCall) {
        RuntimeExpression caller = evaluate(functionCall.getCaller());
        String functionName = functionCall.getFunctionName();
        TokenPosition pos = functionCall.getPosition();

        // Map parameters
        List<RuntimeExpression> parameters = new ArrayList<>();
        for(ExpressionNode param : functionCall.getArguments()) {
            parameters.add(evaluate(param));
        }

        // No caller : "global" function
        if(caller == null) {
            // Is it a java function ?
            RunnableJavaFunction function = JavaFunctionProvider.instance().find(functionName);
            if(function != null) {
                add(new JavaFunctionCallNode(pos, function, parameters));
            }
            // User-defined function instead
            else {
                add(new GlobalFunctionCallNode(pos, functionName, parameters));
            }
            return;
        }

        // A caller : struct-function call.
        StructDefinition callerStruct = functionCall.getCallerStruct();
        add(new StructFunctionCallNode(pos, caller, callerStruct, functionName, parameters));
    }

    @Override
    public void handleIncrementDecrement(@NotNull IncrementExpression expression) {
        String varName = expression.getVarName();
        boolean increment = expression.isPositive();
        boolean postFix = expression.isAfterVar();
        add(new IncrementNode(varName, increment, postFix));
    }

    private void add(RuntimeExpression expression) {
        currentExpressions.add(expression);
    }

    @Contract("null -> null; !null -> new")
    private RuntimeExpression evaluate(ExpressionNode dsl) {
        if(dsl == null) return null;
        currentExpressions = new ArrayDeque<>();
        dsl.visit(this);
        RuntimeExpression node = currentExpressions.pop();
        currentExpressions = expressions;
        return node;
    }

}
