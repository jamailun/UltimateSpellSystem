package fr.jamailun.ultimatespellsystem.runner.builder;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import fr.jamailun.ultimatespellsystem.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.runner.nodes.blocks.BlockNodes;
import fr.jamailun.ultimatespellsystem.runner.nodes.blocks.RunLaterNode;
import fr.jamailun.ultimatespellsystem.runner.nodes.blocks.RunRepeatNode;
import fr.jamailun.ultimatespellsystem.runner.nodes.functions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A visitor used to build a runtime-node tree from the DSL.
 * @see #build(List)
 */
public class SpellBuilderVisitor implements StatementVisitor {

    private final ExpressionQueue queue = new ExpressionQueue();
    private List<RuntimeStatement> currentQueue;
    private final List<RuntimeStatement> statements = new ArrayList<>();

    public SpellBuilderVisitor() {
        currentQueue = statements;
    }

    public static List<RuntimeStatement> build(List<StatementNode> dsl) {
        SpellBuilderVisitor visitor = new SpellBuilderVisitor();
        for(StatementNode statement : dsl) {
            statement.visit(visitor);
        }
        return visitor.statements;
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
        RuntimeStatement child = convertOneStatement(statement.getStatement());
        add(new RunLaterNode(duration, child));
    }

    @Override
    public void handleRepeatRun(RepeatStatement statement) {
        RuntimeExpression period = convert(statement.getPeriod());
        RuntimeStatement child = convertOneStatement(statement.getStatement());
        RuntimeExpression delay = convert(statement.getDelay().orElse(null));
        RuntimeExpression count = convert(statement.getCount());
        add(new RunRepeatNode(period, child, delay, count));
    }

    @Override
    public void handleSummon(SummonStatement statement) {
        RuntimeExpression type = convert(statement.getEntityType());
        RuntimeExpression duration = convert(statement.getDuration());
        RuntimeExpression properties = convert(statement.getProperties().orElse(null));
        String varName = statement.getVarName().orElse(null);
        add(new SummonNode(type, duration, properties, varName));
    }

    @Override
    public void handleBlock(BlockStatement statement) {
        currentQueue = new ArrayList<>();
        for(StatementNode child : statement.getChildren()) {
            child.visit(this);
        }
        BlockNodes block = new BlockNodes(currentQueue);
        currentQueue = statements;
        add(block);
    }

    private RuntimeExpression convert(ExpressionNode expression) {
        if(expression == null)
            return null;
        expression.visit(queue);
        return queue.fetch();
    }

    private void add(RuntimeStatement rs) {
        currentQueue.add(rs);
    }

    private RuntimeStatement convertOneStatement(StatementNode dsl) {
        if(dsl == null)
            return null;
        currentQueue = new ArrayList<>();
        dsl.visit(this);
        if(currentQueue.size() > 1)
            throw new RuntimeException("Got a size > 1 ! " + currentQueue);
        RuntimeStatement node = currentQueue.get(0);
        currentQueue = statements;
        return node;
    }
}
