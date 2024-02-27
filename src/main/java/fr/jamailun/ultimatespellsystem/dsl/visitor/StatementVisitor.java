package fr.jamailun.ultimatespellsystem.dsl.visitor;

import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.flow.ElseStatement;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.flow.IfStatement;

/**
 * A visitor. Can interact with the node-tree.
 */
public interface StatementVisitor {

    void handleStop(StopStatement statement);
    void handleSendMessage(SendMessageStatement statement);
    void handleSendEffect(SendEffectStatement statement);
    void handleDefine(DefineStatement statement);
    void handleRunLater(RunLaterStatement statement);
    void handleRepeatRun(RepeatStatement statement);
    void handleSummon(SummonStatement statement);
    void handleBlock(BlockStatement statement);

    void handleIf(IfStatement statement);
    void handleElse(ElseStatement statement);

}
