package fr.jamailun.ultimatespellsystem.bukkit.runner.builder;

import fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.blocks.*;
import fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions.DefineNode;
import fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators.IncrementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * A visitor used to build a runtime-node tree from the DSL.
 * @see #build(List)
 */
public class SpellBuilderVisitor implements StatementVisitor {

    private final ExpressionQueue expressionQueue = new ExpressionQueue();
    private List<RuntimeStatement> currentQueue; // dynamic pointer toward the top of the queue stack.
    private final List<RuntimeStatement> statementsAccumulator = new ArrayList<>();
    private final Deque<List<RuntimeStatement>> accumulatorsStack = new ArrayDeque<>();

    public SpellBuilderVisitor() {
        currentQueue = statementsAccumulator;
    }

    public static List<RuntimeStatement> build(List<StatementNode> dsl) {
        SpellBuilderVisitor visitor = new SpellBuilderVisitor();
        for(StatementNode statement : dsl) {
            statement.visit(visitor);
        }
        return visitor.statementsAccumulator;
    }

    @Override
    public void handleStop(StopStatement statement) {
        add(new StopNode());
    }

    @Override
    public void handleSendMessage(SendMessageStatement statement) {
        RuntimeExpression target = convert(statement.getTarget());
        RuntimeExpression message = convert(statement.getMessage());
        add(new SendMessageNode(target, message));
    }

    @Override
    public void handleSendEffect(SendEffectStatement statement) {
        RuntimeExpression target = convert(statement.getTarget());
        RuntimeExpression effect = convert(statement.getEffectType());
        RuntimeExpression power = convert(statement.getEffectPower().orElse(null));
        RuntimeExpression duration = convert(statement.getEffectDuration());
        add(new SendEffectNode(target, effect, duration, power));
    }

    @Override
    public void handleDefine(DefineStatement statement) {
        String varName = statement.getVarName();
        RuntimeExpression value = convert(statement.getExpression());
        add(new DefineNode(varName, value, statement.getExpression().getExpressionType()));
    }

    @Override
    public void handleRunLater(RunLaterStatement statement) {
        RuntimeExpression duration = convert(statement.getDuration());
        RuntimeStatement child = convertOneStatement(statement.getChild());
        add(new RunLaterNode(duration, child));
    }

    @Override
    public void handleRepeatRun(RepeatStatement statement) {
        RuntimeExpression period = convert(statement.getPeriod());
        RuntimeStatement child = convertOneStatement(statement.getChild());
        RuntimeExpression delay = convert(statement.getDelay().orElse(null));
        RuntimeExpression count = convert(statement.getCount());
        add(new RunRepeatNode(period, child, delay, count));
    }

    @Override
    public void handleSummon(SummonStatement statement) {
        RuntimeExpression type = convert(statement.getEntityType());
        RuntimeExpression source = convert(statement.getSource().orElse(null));
        RuntimeExpression duration = convert(statement.getDuration());
        RuntimeExpression properties = convert(statement.getProperties().orElse(null));
        String varName = statement.getVarName().orElse(null);
        add(new SummonNode(type, source, duration, properties, varName));
    }

    @Override
    public void handleBlock(BlockStatement statement) {
        pushQueue();

        for(StatementNode child : statement.getChildren()) {
            child.visit(this);
        }
        BlockNodes block = new BlockNodes(currentQueue);

        popQueue();

        add(block);
    }

    @Override
    public void handleIncrement(IncrementStatement statement) {
        String varName = statement.getVarName();
        boolean increments = statement.isPositive();
        add(new IncrementNode(varName, increments));
    }

    @Override
    public void handleTeleport(TeleportStatement statement) {
        RuntimeExpression entity = convert(statement.getEntity());
        RuntimeExpression target = convert(statement.getTarget());
        add(new TeleportNode(entity, target));
    }

    @Override
    public void handlePlayParticle(PlayParticleStatement statement) {
        //TODO
    }

    @Override
    public void handlePlayBlock(PlayBlockStatement statement) {
        //TODO
    }

    @Override
    public void handleIf(IfElseStatement statement) {
        RuntimeExpression condition = convert(statement.getCondition());
        RuntimeStatement childTrue = convertOneStatement(statement.getChild());
        RuntimeStatement childFalse = convertOneStatement(statement.getElse().orElse(null));
        add(new IfElseNode(condition, childTrue, childFalse));
    }

    @Override
    public void handleForLoop(ForLoopStatement statement) {
        RuntimeStatement init = convertOneStatement(statement.getInitialization());
        RuntimeExpression condition = convert(statement.getCondition());
        RuntimeStatement iteration = convertOneStatement(statement.getIteration());
        RuntimeStatement child = convertOneStatement(statement.getChild());
        add(new ForLoopNode(init, condition, iteration, child));
    }

    @Override
    public void handleForeachLoop(ForeachLoopStatement statement) {
        String varName = statement.getVariableName();
        RuntimeExpression source = convert(statement.getSource());
        RuntimeStatement child = convertOneStatement(statement.getChild());
        add(new ForeachLoopNode(varName, source, child));

    }

    @Override
    public void handleWhileLoop(WhileLoopStatement statement) {
        RuntimeExpression condition = convert(statement.getCondition());
        RuntimeStatement child = convertOneStatement(statement.getChild());
        boolean whileFirst = statement.isWhileFirst();
        add(new WhileLoopNode(condition, child, whileFirst));
    }

    private RuntimeExpression convert(ExpressionNode expression) {
        if(expression == null)
            return null;
        expression.visit(expressionQueue);
        return expressionQueue.fetch();
    }

    private void add(RuntimeStatement rs) {
        currentQueue.add(rs);
    }

    private RuntimeStatement convertOneStatement(@Nullable StatementNode dsl) {
        if(dsl == null)
            return null;
        pushQueue();

        dsl.visit(this);
        if(currentQueue.size() > 1)
            throw new RuntimeException("Got a size > 1 ! " + currentQueue);
        RuntimeStatement node = currentQueue.get(0);

        popQueue();
        return node;
    }

    private void pushQueue() {
        accumulatorsStack.push(currentQueue);
        currentQueue = new ArrayList<>();
        //System.out.println("PUSH-Queue ! curr="+currentQueue+", QQ="+ accumulatorsStack);
    }

    private void popQueue() {
        currentQueue = accumulatorsStack.pop();
        //System.out.println("POP-Queue ! curr="+currentQueue+", QQ="+ accumulatorsStack);
    }
}
