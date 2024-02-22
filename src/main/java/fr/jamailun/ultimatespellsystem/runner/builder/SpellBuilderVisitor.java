package fr.jamailun.ultimatespellsystem.runner.builder;

import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import fr.jamailun.ultimatespellsystem.runner.RuntimeExpression;


public class SpellBuilderVisitor implements StatementVisitor {


    private ExpressionQueue queue = new ExpressionQueue();


    @Override
    public void handleStop(StopStatement statement) {

    }

    @Override
    public void handleSendMessage(SendMessageStatement statement) {

    }

    @Override
    public void handleSendEffect(SendEffectStatement statement) {

    }

    @Override
    public void handleDefine(DefineStatement statement) {

    }

    @Override
    public void handleRunLater(RunLaterStatement statement) {

    }

    @Override
    public void handleRepeatRun(RepeatStatement statement) {

    }

    @Override
    public void handleSummon(SummonStatement statement) {

    }
}
