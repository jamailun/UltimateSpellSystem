package fr.jamailun.ultimatespellsystem.dsl.visitor;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;

import java.util.List;

public class PrintingVisitor implements StatementVisitor {

    private final int indentDelta;
    private String indentBuffer;
    private int currentIndent = 0;
    private boolean dirty = true;

    private StringBuilder builder;

    public PrintingVisitor(int indent) {
        this.indentDelta = indent;
    }

    public String visit(List<StatementNode> statements) {
        builder = new StringBuilder();
        for(StatementNode statement : statements) {
            statement.visit(this);
        }
        return builder.toString();
    }

    private String indent() {
        if(dirty) {
            indentBuffer = " ".repeat(currentIndent);
            dirty = false;
        }
        return indentBuffer;
    }

    private void right() {
        currentIndent += indentDelta;
        dirty = true;
    }

    private void left() {
        currentIndent -= indentDelta;
        if(currentIndent < 0) {
            currentIndent = 0;
            System.err.println("Bad indent !");
        }
        dirty = true;
    }

    @Override
    public void handleStop(StopStatement statement) {
        builder.append(indent()).append("stop;");
    }

    @Override
    public void handleSendMessage(SendMessageStatement statement) {
        builder.append(indent()).append("send to ");
        ExpressionNode target = statement.getTarget();

        //.append(statement.get)
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
