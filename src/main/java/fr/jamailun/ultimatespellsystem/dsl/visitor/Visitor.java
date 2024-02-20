package fr.jamailun.ultimatespellsystem.dsl.visitor;

import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;

public interface Visitor {

    void handleStop(StopStatement statement);
    void handleSendMessage(SendMessageStatement statement);
    void handleSendEffect(SendEffectStatement statement);
    void handleDefine(DefineStatement statement);
    void handleRunLater(RunLaterStatement statement);
    void handleRepeatRun(RepeatStatement statement);

}
