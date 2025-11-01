package fr.jamailun.ultimatespellsystem.plugin.runner.builder;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks.*;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.statements.ExpressionWrapperNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.*;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.StatementVisitor;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.RuntimeFunctionDeclaration;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.statements.AffectVarNode;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.statements.ReturnNode;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.statements.DeclareVarNode;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A visitor used to build a runtime-node tree from the DSL.
 * @see #build(List)
 */
public class SpellBuilderVisitor implements StatementVisitor {

    private final ExpressionQueue expressionQueue = new ExpressionQueue();
    @Getter private List<RuntimeStatement> currentQueue; // dynamic pointer toward the top of the queue stack.
    private final List<RuntimeStatement> statementsAccumulator = new ArrayList<>();
    private final Deque<List<RuntimeStatement>> accumulatorsStack = new ArrayDeque<>();

    @Getter private final Map<String, RuntimeFunctionDeclaration> functions = new HashMap<>();

    public SpellBuilderVisitor() {
        currentQueue = statementsAccumulator;
    }

    public static @NotNull List<RuntimeStatement> build(@NotNull List<StatementNode> dsl) {
        SpellBuilderVisitor visitor = new SpellBuilderVisitor();
        for(StatementNode statement : dsl) {
            statement.visit(visitor);
        }
        return visitor.statementsAccumulator;
    }

    @Override
    public void handleDeclareVariable(@NotNull DeclareNewVariableStatement statement) {
        String varName = statement.getVarName();
        Type type = statement.getCompiledType();
        RuntimeExpression expression = convert(statement.getExpression());
        add(new DeclareVarNode(varName, type, expression));
    }

    @Override
    public void handleAffectVariable(@NotNull AffectationStatement statement) {
        RuntimeExpression holder = convert(statement.getValueHolder());
        RuntimeExpression value = convert(statement.getExpression());
        add(new AffectVarNode(holder, value));
    }

    @Override
    public void handleFunctionDeclaration(@NotNull FunctionDeclarationStatement statement) {
        // Basics
        String funcName = statement.getFunctionName();
        Type type = statement.getOutputType();

        // Convert statements
        pushQueue();
        for(StatementNode child : statement.getStatements()) {
            child.visit(this);
        }
        List<RuntimeStatement> statements = List.copyOf(currentQueue);
        popQueue();

        // Register function
        functions.put(funcName, new RuntimeFunctionDeclaration(funcName, type, statement.getParameters(), statements));
    }

    @Override
    public void handleReturn(@NotNull ReturnStatement statement) {
        RuntimeExpression exitCode = convert(statement.getExitCodeNode());
        add(new ReturnNode(exitCode));
    }

    @Override
    public void handleBlock(@NotNull BlockStatement statement) {
        pushQueue();

        for(StatementNode child : statement.getChildren()) {
            child.visit(this);
        }
        BlockNodes block = new BlockNodes(currentQueue);
        popQueue();

        add(block);
    }

    @Override
    public void handleSimpleExpression(@NotNull SimpleExpressionStatement statement) {
        RuntimeExpression child = convert(statement.getChild());
        add(new ExpressionWrapperNode(child));
    }

    @Override
    public void handleIf(@NotNull IfElseStatement statement) {
        RuntimeExpression condition = convert(statement.getCondition());
        RuntimeStatement childTrue = convertOneStatement(statement.getChild());
        RuntimeStatement childFalse = convertOneStatement(statement.getElse().orElse(null));
        add(new IfElseNode(condition, childTrue, childFalse));
    }

    @Override
    public void handleForLoop(@NotNull ForLoopStatement statement) {
        RuntimeStatement init = convertOneStatement(statement.getInitialization());
        RuntimeExpression condition = convert(statement.getCondition());
        RuntimeStatement iteration = convertOneStatement(statement.getIteration());
        RuntimeStatement child = convertOneStatement(statement.getChild());
        add(new ForLoopNode(init, condition, iteration, child));
    }

    @Override
    public void handleWhileLoop(@NotNull WhileLoopStatement statement) {
        RuntimeExpression condition = convert(statement.getCondition());
        RuntimeStatement child = convertOneStatement(statement.getChild());
        boolean whileFirst = statement.isWhileFirst();
        add(new WhileLoopNode(condition, child, whileFirst));
    }

    @Override
    public void handleBreakContinue(@NotNull BreakContinueStatement statement) {
        add(new BreakContinueNode(statement.isContinue()));
    }

    @Contract("null -> null; !null -> new")
    public RuntimeExpression convert(ExpressionNode expression) {
        if(expression == null)
            return null;
        expression.visit(expressionQueue);
        return expressionQueue.fetch();
    }

    private void add(RuntimeStatement rs) {
        currentQueue.add(rs);
    }

    @Contract("null -> null; !null -> !null")
    private RuntimeStatement convertOneStatement(@Nullable StatementNode dsl) {
        if(dsl == null)
            return null;
        pushQueue();

        dsl.visit(this);
        if(currentQueue.size() > 1)
            throw new RuntimeException("Got a size > 1 ! " + currentQueue);
        RuntimeStatement node = currentQueue.getFirst();

        popQueue();
        return node;
    }

    private void pushQueue() {
        accumulatorsStack.push(currentQueue);
        currentQueue = new ArrayList<>();
    }

    private void popQueue() {
        currentQueue = accumulatorsStack.pop();
    }
}
