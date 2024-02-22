package fr.jamailun.ultimatespellsystem.dsl.visitor;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.AllEntitiesAround;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.ArrayConcatExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.PropertiesExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.VariableExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

import java.util.List;

public class PrintingVisitor implements StatementVisitor, ExpressionVisitor {

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

    @Override
    public void handleNullLiteral(NullExpression literal) {

    }

    @Override
    public void handleBooleanLiteral(BooleanExpression literal) {

    }

    @Override
    public void handleNumberLiteral(NumberExpression literal) {

    }

    @Override
    public void handleStringLiteral(StringExpression literal) {

    }

    @Override
    public void handleEntityTypeLiteral(EntityTypeExpression literal) {

    }

    @Override
    public void handleRuntimeLiteral(RuntimeLiteral literal) {

    }

    @Override
    public void handleDurationLiteral(DurationExpression literal) {

    }

    @Override
    public void handleEffectLiteral(EffectTypeExpression literal) {

    }

    @Override
    public void handlePropertiesSet(PropertiesExpression expression) {

    }

    @Override
    public void handleAllAround(AllEntitiesAround expression) {

    }

    @Override
    public void handleArrayConcat(ArrayConcatExpression expression) {

    }

    @Override
    public void handleVariable(VariableExpression expression) {

    }
}
