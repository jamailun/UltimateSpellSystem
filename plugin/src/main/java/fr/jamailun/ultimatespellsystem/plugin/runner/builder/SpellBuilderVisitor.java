package fr.jamailun.ultimatespellsystem.plugin.runner.builder;

import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.MetadataNode;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks.*;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.expressions.ExpressionWrapperNode;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.play.PlayBlockNode;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.play.PlayNode;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.play.PlayParticleNode;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.play.PlaySoundNode;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.operators.IncrementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.*;
import org.jetbrains.annotations.NotNull;
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

    public static @NotNull List<RuntimeStatement> build(@NotNull List<StatementNode> dsl) {
        SpellBuilderVisitor visitor = new SpellBuilderVisitor();
        for(StatementNode statement : dsl) {
            statement.visit(visitor);
        }
        return visitor.statementsAccumulator;
    }

    @Override
    public void handleStop(@NotNull StopStatement statement) {
        RuntimeExpression exitCode = convert(statement.getExitCodeNode());
        add(new StopNode(exitCode));
    }

    @Override
    public void handleSendMessage(@NotNull SendMessageStatement statement) {
        RuntimeExpression target = convert(statement.getTarget());
        RuntimeExpression message = convert(statement.getMessage());
        add(new SendMessageNode(target, message));
    }

    @Override
    public void handleSendEffect(@NotNull SendEffectStatement statement) {
        RuntimeExpression target = convert(statement.getTarget());
        RuntimeExpression effect = convert(statement.getEffectType());
        RuntimeExpression power = convert(statement.getEffectPower().orElse(null));
        RuntimeExpression duration = convert(statement.getEffectDuration());
        add(new SendEffectNode(target, effect, duration, power));
    }

    @Override
    public void handleSendAttribute(@NotNull SendAttributeStatement statement) {
        RuntimeExpression target = convert(statement.getTarget());
        RuntimeExpression value = convert(statement.getNumericValue());
        RuntimeExpression type = convert(statement.getAttributeType());
        RuntimeExpression mode = convert(statement.getAttributeMode().orElse(null));
        RuntimeExpression duration = convert(statement.getDuration());
        add(new SendAttributeNode(target, value, type, mode, duration));

    }

    @Override
    public void handleDefine(@NotNull DefineStatement statement) {
        String varName = statement.getVarName();
        RuntimeExpression value = convert(statement.getExpression());
        add(new DefineNode(varName, value, statement.getExpression().getExpressionType()));
    }

    @Override
    public void handleRunLater(@NotNull RunLaterStatement statement) {
        RuntimeExpression duration = convert(statement.getDuration());
        RuntimeStatement child = convertOneStatement(statement.getChild());
        add(new RunLaterNode(duration, child));
    }

    @Override
    public void handleRepeatRun(@NotNull RepeatStatement statement) {
        RuntimeExpression period = convert(statement.getPeriod());
        RuntimeStatement child = convertOneStatement(statement.getChild());
        RuntimeExpression delay = convert(statement.getDelay().orElse(null));
        RuntimeExpression count = convert(statement.getCount());
        add(new RunRepeatNode(period, child, delay, count));
    }

    @Override
    public void handleSummon(@NotNull SummonStatement statement) {
        RuntimeExpression type = convert(statement.getEntityType());
        RuntimeExpression source = convert(statement.getSource().orElse(null));
        RuntimeExpression duration = convert(statement.getDuration());
        RuntimeExpression properties = convert(statement.getProperties().orElse(null));
        String varName = statement.getVarName().orElse(null);
        add(new SummonNode(type, source, duration, properties, varName));
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
    public void handleIncrement(@NotNull IncrementStatement statement) {
        String varName = statement.getVarName();
        boolean increments = statement.isPositive();
        add(new IncrementNode(varName, increments));
    }

    @Override
    public void handleTeleport(@NotNull TeleportStatement statement) {
        RuntimeExpression entity = convert(statement.getEntity());
        RuntimeExpression target = convert(statement.getTarget());
        add(new TeleportNode(entity, target));
    }

    @Override
    public void handlePlay(@NotNull PlayStatement statement) {
        RuntimeExpression location = convert(statement.getLocation());
        RuntimeExpression properties = convert(statement.getProperties());
        PlayNode node = switch (statement.getType()) {
            case BLOCK -> new PlayBlockNode(location, properties);
            case PARTICLE -> new PlayParticleNode(location, properties);
            case SOUND -> new PlaySoundNode(location, properties);
        };
        add(node);
    }

    @Override
    public void handleGive(@NotNull GiveStatement statement) {
        RuntimeExpression target = convert(statement.getTarget());
        RuntimeExpression amount = convert(statement.getOptAmount());
        RuntimeExpression type = convert(statement.getOptType());
        RuntimeExpression properties = convert(statement.getOptProperties());
        add(new GiveNode(target, amount, type, properties));
    }

    @Override
    public void handleSimpleExpression(@NotNull SimpleExpressionStatement statement) {
        RuntimeExpression child = convert(statement.getChild());
        add(new ExpressionWrapperNode(child));
    }

    @Override
    public void handleMetadata(@NotNull MetadataStatement statement) {
        String name = statement.getName();
        add(new MetadataNode(name, statement.getParams()));
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
    public void handleForeachLoop(@NotNull ForeachLoopStatement statement) {
        String varName = statement.getVariableName();
        RuntimeExpression source = convert(statement.getSource());
        RuntimeStatement child = convertOneStatement(statement.getChild());
        add(new ForeachLoopNode(varName, source, child));

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
        RuntimeStatement node = currentQueue.getFirst();

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
