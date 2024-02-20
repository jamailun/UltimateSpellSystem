package fr.jamailun.ultimatespellsystem.dsl.visitor;

import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.SendEffectStatement;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.SendMessageStatement;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.StopStatement;

public interface Visitor {


    void handleStop(StopStatement stopStatement);

    void handleSendMessage(SendMessageStatement messageStatement);

    void handleSendEffect(SendEffectStatement effectStatement);

}
