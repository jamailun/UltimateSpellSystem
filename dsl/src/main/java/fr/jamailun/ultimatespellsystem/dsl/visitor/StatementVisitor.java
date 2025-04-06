package fr.jamailun.ultimatespellsystem.dsl.visitor;

import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;
import org.jetbrains.annotations.NotNull;

/**
 * A visitor. Can interact with the node-tree.
 */
public interface StatementVisitor {

    void handleStop(@NotNull StopStatement statement);
    void handleSendAttribute(@NotNull SendAttributeStatement statement);
    void handleSendEffect(@NotNull SendEffectStatement statement);
    void handleSendMessage(@NotNull SendMessageStatement statement);
    void handleSendNbt(@NotNull SendNbtStatement statement);
    void handleDefine(@NotNull DefineStatement statement);
    void handleRunLater(@NotNull RunLaterStatement statement);
    void handleRepeatRun(@NotNull RepeatStatement statement);
    void handleSummon(@NotNull SummonStatement statement);
    void handleBlock(@NotNull BlockStatement statement);
    void handleIncrement(@NotNull IncrementStatement statement);
    void handleTeleport(@NotNull TeleportStatement statement);
    void handlePlay(@NotNull PlayStatement statement);
    void handleGive(@NotNull GiveStatement statement);
    void handleCallback(@NotNull CallbackStatement statement);
    void handleSimpleExpression(@NotNull SimpleExpressionStatement statement);
    void handleMetadata(@NotNull MetadataStatement statement);

    void handleIf(@NotNull IfElseStatement statement);
    void handleForLoop(@NotNull ForLoopStatement statement);
    void handleForeachLoop(@NotNull ForeachLoopStatement statement);
    void handleWhileLoop(@NotNull WhileLoopStatement statement);
    void handleBreakContinue(@NotNull BreakContinueStatement statement);
}
