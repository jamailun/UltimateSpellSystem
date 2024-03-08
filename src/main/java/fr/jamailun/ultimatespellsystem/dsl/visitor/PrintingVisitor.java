package fr.jamailun.ultimatespellsystem.dsl.visitor;

import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.AllEntitiesAroundExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.PositionOfExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.BiOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.MonoOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class PrintingVisitor implements StatementVisitor, ExpressionVisitor {

    private final static DecimalFormat formatNumber = new DecimalFormat("#.##");

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
        builder.append("function __spell__(%caster) {\n");

        right();
        for(StatementNode statement : statements) {
            statement.visit(this);
            eol();
        }
        left();

        builder.append("}");
        return builder.toString();
    }

    public static void print(List<StatementNode> statements) {
        print(statements, 2);
    }

    public static void print(List<StatementNode> statements, int indent) {
        System.out.println(toString(statements, indent));
    }

    public static String toString(List<StatementNode> statements) {
        return toString(statements, 2);
    }

    public static String toString(List<StatementNode> statements, int indent) {
        return new PrintingVisitor(indent).visit(statements);
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
        }
        dirty = true;
    }

    private void eol() {
        builder.append(";\n");
    }

    @Override
    public void handleStop(StopStatement statement) {
        builder.append(indent()).append("stop");
    }

    @Override
    public void handleSendMessage(SendMessageStatement statement) {
        builder.append(indent()).append("send to ");
        statement.getTarget().visit(this);
        builder.append(" message ");
        statement.getMessage().visit(this);
    }

    @Override
    public void handleSendEffect(SendEffectStatement statement) {
        builder.append(indent()).append("send to ");
        statement.getTarget().visit(this);
        builder.append(" effect ");
        statement.getEffectType().visit(this);
        statement.getEffectPower().ifPresent(e -> {
            builder.append(" ");
            e.visit(this);
        });
        builder.append(" for ");
        statement.getEffectDuration().visit(this);
    }

    @Override
    public void handleDefine(DefineStatement statement) {
        builder.append(indent())
                .append("define %")
                .append(statement.getVarName())
                .append(" as: ");
        statement.getExpression().visit(this);
    }

    @Override
    public void handleRunLater(RunLaterStatement statement) {
        builder.append(indent()).append("run after ");
        statement.getDuration().visit(this);
        builder.append(" : ");
        statement.getChild().visit(this);
    }

    @Override
    public void handleRepeatRun(RepeatStatement statement) {
        builder.append(indent()).append("run ");
        statement.getCount().visit(this);
        builder.append(" times");
        statement.getDelay().ifPresent(e -> {
            builder.append(" after");
            e.visit(this);
        });
        builder.append(" every ");
        statement.getPeriod().visit(this);
        builder.append(" : ");
        statement.getChild().visit(this);
    }

    @Override
    public void handleSummon(SummonStatement statement) {
        builder.append(indent()).append("summon ");
        statement.getEntityType().visit(this);
        statement.getSource().ifPresent(p -> {
            builder.append(" at ");
            p.visit(this);
        });
        builder.append(" for ");
        statement.getDuration().visit(this);
        statement.getVarName().ifPresent(v -> builder.append(" as %").append(v));
        statement.getProperties().ifPresent(p -> {
            builder.append(" with: ");
            p.visit(this);
        });
    }

    @Override
    public void handleBlock(BlockStatement statement) {
        builder.append("{\n");
        right();
        for(StatementNode child : statement.getChildren()) {
            child.visit(this);
            eol();
        }
        left();
        builder.append(indent()).append("}");
    }

    @Override
    public void handleIncrement(IncrementStatement statement) {
        builder.append(statement.isPositive() ? "++" : "--")
                .append("%").append(statement.getVarName());
    }

    @Override
    public void handleTeleport(TeleportStatement statement) {
        builder.append("teleport ");
        statement.getEntity().visit(this);
        builder.append(" to ");
        statement.getTarget().visit(this);
    }

    @Override
    public void handlePlay(PlayStatement statement) {
        builder.append("PLAY ")
                .append(statement.getType())
                .append(" AT ");
        statement.getLocation().visit(this);
        builder.append(" WITH ");;
        statement.getProperties().visit(this);
    }

    @Override
    public void functionCall(FunctionCallStatement statement) {
        builder.append("CALL ")
                .append(statement.getFunctionId())
                .append(" (");
        boolean first = true;
        for(ExpressionNode arg : statement.getArguments()) {
            if(first) first = false; else builder.append(", ");
            arg.visit(this);
        }
        builder.append(")");
    }

    @Override
    public void handleIf(IfElseStatement statement) {
        builder.append(indent()).append("IF(");
        statement.getCondition().visit(this);
        builder.append("):");
        right();
        statement.getChild().visit(this);
        left();
        builder.append("\n");

        statement.getElse().ifPresent(e -> {
            builder.append(indent()).append("ELSE:");
            right();
            e.visit(this);
            left();
            builder.append("\n");
        });
    }

    @Override
    public void handleForLoop(ForLoopStatement statement) {
        builder.append("FOR(");
        statement.getInitialization().visit(this);
        statement.getCondition().visit(this);
        builder.append(";");
        statement.getInitialization().visit(this);
        builder.append("):");
        statement.getChild().visit(this);
    }

    @Override
    public void handleForeachLoop(ForeachLoopStatement statement) {
        builder.append("FOREACH(%")
                .append(statement.getVariableName())
                .append(" : ");
        statement.getSource().visit(this);
        builder.append("):");
        statement.getChild().visit(this);
    }

    @Override
    public void handleWhileLoop(WhileLoopStatement statement) {
        if(statement.isWhileFirst()) {
            builder.append("WHILE(");
            statement.getCondition().visit(this);
            builder.append(") ");
        } else {
            builder.append("DO ");
        }
        statement.getChild().visit(this);
        if(!statement.isWhileFirst()) {
            builder.append(" WHILE(");
            statement.getCondition().visit(this);
            builder.append(") ");
        }
    }

    @Override
    public void handleNullLiteral(NullExpression literal) {
        builder.append("NULL");
    }

    @Override
    public void handleBooleanLiteral(BooleanExpression literal) {
        builder.append(literal.getRaw());
    }

    @Override
    public void handleNumberLiteral(NumberExpression literal) {
        double num = literal.getRaw();
        builder.append(formatNumber.format(num));
    }

    @Override
    public void handleStringLiteral(StringExpression literal) {
        builder.append("\"").append(literal.getRaw()).append("\"");
    }

    @Override
    public void handleEntityTypeLiteral(EntityTypeExpression literal) {
        builder.append("EntityType.").append(literal.getRaw());
    }

    @Override
    public void handleRuntimeLiteral(RuntimeLiteral literal) {
        builder.append("'").append(literal.getRaw()).append("'");
    }

    @Override
    public void handleDurationLiteral(DurationExpression literal) {
        Duration duration = literal.getRaw();
        builder.append(formatNumber.format(duration.amount()))
                .append(" ")
                .append(duration.niceUnit());
    }

    @Override
    public void handleEffectLiteral(EffectTypeExpression literal) {
        builder.append("EffectType.").append(literal.getRaw());
    }

    @Override
    public void handleBiOperator(BiOperator operator) {
        operator.getLeft().visit(this);
        String ope = switch (operator.getType()) {
            case ADD -> "+";
            case SUB -> "-";
            case MUL -> "*";
            case DIV -> "/";
            case EQUAL -> "==";
            case NOT_EQUAL -> "!=";
            case GREATER_OR_EQ -> ">=";
            case GREATER -> ">";
            case LESSER_OR_EQ -> "<=";
            case LESSER -> "<";
            case AND -> "and";
            case OR -> "or";
        };
        builder.append(" ").append(ope).append(" ");
        operator.getRight().visit(this);
    }

    @Override
    public void handleMonoOperator(MonoOperator operator) {
        String ope = operator.getType().name();
        builder.append(ope).append("(");
        operator.getChild().visit(this);
        builder.append(")");
    }

    @Override
    public void handleParenthesis(ParenthesisExpression parenthesis) {
        builder.append("(");
        parenthesis.getExpression().visit(this);
        builder.append(")");
    }

    @Override
    public void handleArrayGet(ArrayGetterExpression arrayGetter) {
        arrayGetter.getArray().visit(this);
        builder.append("[");
        arrayGetter.getIndex().visit(this);
        builder.append("]");
    }

    @Override
    public void handlePropertiesSet(PropertiesExpression expression) {
        builder.append("{{");
        if(expression.getExpressions().isEmpty()) {
            builder.append("}}");
            return;
        }
        builder.append("\n");
        right();
        boolean first = true;
        for(Map.Entry<String, ExpressionNode> entry : expression.getExpressions().entrySet()) {
            if(first) first = false; else builder.append(",\n");
            builder.append(indent())
                    .append(entry.getKey())
                    .append(" = ");
            entry.getValue().visit(this);
        }
        builder.append("\n");
        left();
        builder.append(indent()).append("}}");
    }

    @Override
    public void handleAllAround(AllEntitiesAroundExpression expression) {
        builder.append("<all ");
        expression.getEntityType().visit(this);
        builder.append(" within ");
        expression.getDistance().visit(this);
        builder.append(" around ");
        expression.getSource().visit(this);
        String add = expression.isIncluding() ? "(including)" : "(excluding)";
        builder.append(" ").append(add).append(">");
    }

    @Override
    public void handlePositionOf(PositionOfExpression expression) {
        builder.append("position of (");
        expression.getEntity().visit(this);
        builder.append(")");
    }

    @Override
    public void handleArray(ArrayExpression expression) {
        builder.append("[");
        boolean first = true;
        for(ExpressionNode child : expression.getElements()) {
            if(first) first = false; else builder.append(", ");
            child.visit(this);
        }
        builder.append("]");
    }

    @Override
    public void handleVariable(VariableExpression expression) {
        builder.append("%").append(expression.getVariableName());
    }
}
