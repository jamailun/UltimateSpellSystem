package fr.jamailun.ultimatespellsystem.dsl.visitor;

import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;

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
    void handleIncrement(IncrementStatement statement);
    void handleTeleport(TeleportStatement statement);
    void handlePlay(PlayStatement statement);
    void functionCall(FunctionCallStatement statement);

    void handleIf(IfElseStatement statement);
    void handleForLoop(ForLoopStatement statement);
    void handleForeachLoop(ForeachLoopStatement statement);
    void handleWhileLoop(WhileLoopStatement statement);
}
