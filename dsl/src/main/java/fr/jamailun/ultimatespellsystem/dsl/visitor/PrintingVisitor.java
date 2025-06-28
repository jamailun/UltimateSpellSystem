package fr.jamailun.ultimatespellsystem.dsl.visitor;

import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionCallExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.compute.SizeOfExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.compute.AllEntitiesAroundExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.compute.PositionOfExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.BiOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.MonoOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Made to print content.
 */
public class PrintingVisitor implements StatementVisitor, ExpressionVisitor {

    private final static DecimalFormat formatNumber = new DecimalFormat("#.##");

    private final int indentDelta;
    private String indentBuffer;
    private int currentIndent = 0;
    private boolean dirty = true;

    private StringBuilder builder;

    private PrintingVisitor(int indent) {
        this.indentDelta = indent;
    }

    private @NotNull String visit(@NotNull List<StatementNode> statements) {
        builder = new StringBuilder();
        for(StatementNode statement : statements) {
            statement.visit(this);
            eol();
        }
        return builder.toString();
    }

    /**
     * Print multiple statements to sys-out, with an indentation of {@code 2}.
     * @param statements the non-null statements to visit.
     */
    public static void print(@NotNull List<StatementNode> statements) {
        print(statements, 2);
    }

    /**
     * Print multiple statements to sys-out.
     * @param statements list of statements to print.
     * @param indent specific indent to use.
     */
    public static void print(@NotNull List<StatementNode> statements, int indent) {
        System.out.println(toString(statements, indent));
    }

    /**
     * Print multiple statements to a String.
     * @param statements list of statements to print.
     * @return the print output.
     */
    public static @NotNull String toString(@NotNull List<StatementNode> statements) {
        return toString(statements, 2);
    }

    /**
     * Print multiple statements to a String.
     * @param statements list of statements to print.
     * @param indent specific indent to use.
     * @return the print output.
     */
    public static @NotNull String toString(@NotNull List<StatementNode> statements, int indent) {
        return new PrintingVisitor(indent).visit(statements);
    }

    private @NotNull String indent() {
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
    public void handleStop(@NotNull StopStatement statement) {
        builder.append(indent()).append("stop");
    }

    @Override
    public void handleSendMessage(@NotNull SendMessageStatement statement) {
        builder.append(indent()).append("send to ");
        statement.getTarget().visit(this);
        builder.append(" message ");
        statement.getMessage().visit(this);
    }

    @Override
    public void handleSendNbt(@NotNull SendNbtStatement statement) {
        builder.append(indent()).append("send to ");
        statement.getTarget().visit(this);
        builder.append(" NBT ");
        statement.getNbtName().visit(this);
        builder.append(" = ");
        statement.getNbtValue().visit(this);
        builder.append(" for ");
        statement.getNbtDuration().visit(this);
    }

    @Override
    public void handleSendEffect(@NotNull SendEffectStatement statement) {
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
    public void handleSendAttribute(@NotNull SendAttributeStatement statement) {
        builder.append(indent()).append("send to ");
        statement.getTarget().visit(this);
        builder.append(" attribute ");
        statement.getNumericValue().visit(this);
        builder.append(" ");
        statement.getAttributeType().visit(this);
        statement.getAttributeMode().ifPresent(e -> {
            builder.append(" (");
            e.visit(this);
            builder.append(")");
        });
        builder.append(" for ");
        statement.getDuration().visit(this);
    }

    @Override
    public void handleDefine(@NotNull DefineStatement statement) {
        builder.append(indent())
                .append("define %")
                .append(statement.getVarName())
                .append(" = ");
        statement.getExpression().visit(this);
    }

    @Override
    public void handleRunLater(@NotNull RunLaterStatement statement) {
        builder.append(indent()).append("run after ");
        statement.getDuration().visit(this);
        builder.append(" : ");
        statement.getChild().visit(this);
    }

    @Override
    public void handleRepeatRun(@NotNull RepeatStatement statement) {
        builder.append(indent()).append("run ");
        statement.getDelay().ifPresent(e -> {
            builder.append("after ");
            e.visit(this);
            builder.append(", ");
        });
        if(statement.getTotalCount() != null) {
            statement.getTotalCount().visit(this);
            builder.append(" times every ");
            statement.getPeriod().visit(this);
        } else {
            assert statement.getTotalDuration() != null;
            builder.append("every ");
            statement.getPeriod().visit(this);
            builder.append(" for ");
            statement.getTotalDuration().visit(this);
        }
        builder.append(" : ");
        statement.getChild().visit(this);
    }

    @Override
    public void handleSummon(@NotNull SummonStatement statement) {
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
    public void handleBlock(@NotNull BlockStatement statement) {
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
    public void handleIncrement(@NotNull IncrementStatement statement) {
        builder.append(statement.isPositive() ? "++" : "--")
                .append("%").append(statement.getVarName());
    }

    @Override
    public void handleTeleport(@NotNull TeleportStatement statement) {
        builder.append("teleport ");
        statement.getEntity().visit(this);
        builder.append(" to ");
        statement.getTarget().visit(this);
    }

    @Override
    public void handlePlay(@NotNull PlayStatement statement) {
        builder.append("PLAY ")
                .append(statement.getType())
                .append(" AT ");
        statement.getLocation().visit(this);
        builder.append(" WITH ");
        statement.getProperties().visit(this);
    }

    @Override
    public void handleGive(@NotNull GiveStatement statement) {
        builder.append("GIVE");
        if(statement.getOptAmount() != null) {
            builder.append(" ");
            statement.getOptAmount().visit(this);
        }
        if(statement.getOptType() != null) {
            builder.append(" ");
            statement.getOptType().visit(this);
        }
        builder.append(" TO ");
        statement.getTarget().visit(this);
        if(statement.getOptProperties() != null) {
            builder.append(" WITH: ");
            statement.getOptProperties().visit(this);
        }
    }

    @Override
    public void handleCallback(@NotNull CallbackStatement statement) {
        builder.append("CALLBACK %")
                .append(statement.getListenVariableName())
                .append(" ON @")
                .append(statement.getCallbackType().name());
        statement.getOutputVariable().ifPresent(output -> {
            assert statement.getCallbackType().argument() != null; // compiler hint
            builder.append(statement.getCallbackType().argument().keyword())
                    .append(" %")
                    .append(output);
        });
        builder.append(": ");
        statement.getChild().visit(this);
    }

    @Override
    public void handleSimpleExpression(@NotNull SimpleExpressionStatement statement) {
        builder.append("{");
        statement.getChild().visit(this);
        builder.append("}");
    }

    @Override
    public void handleMetadata(@NotNull MetadataStatement statement) {
        builder.append(indent()).append("@").append(statement.getName());
        boolean big = statement.getParams().size() > 1;
        if(big) builder.append("(");
        boolean first = true;
        for(Object node : statement.getParams()) {
            if(first) first = false; else builder.append(", ");
            builder.append(node);
        }
        if(big) builder.append(")");
    }

    @Override
    public void handleIf(@NotNull IfElseStatement statement) {
        builder.append(indent()).append("IF(");
        statement.getCondition().visit(this);
        builder.append("):");
        right();
        statement.getChild().visit(this);
        left();
        builder.append("\n");

        statement.getElse().ifPresent(e -> {
            builder.append(indent()).append("ELSE : ");
            right();
            e.visit(this);
            left();
            builder.append("\n");
        });
    }

    @Override
    public void handleForLoop(@NotNull ForLoopStatement statement) {
        builder.append(indent())
            .append("FOR(");
        if(statement.getInitialization() != null) statement.getInitialization().visit(this);
        statement.getCondition().visit(this);
        builder.append(";");
        if(statement.getIteration() != null) statement.getIteration().visit(this);
        builder.append("):");
        statement.getChild().visit(this);
    }

    @Override
    public void handleForeachLoop(@NotNull ForeachLoopStatement statement) {
        builder.append(indent())
                .append("FOREACH(%")
                .append(statement.getVariableName())
                .append(" : ");
        statement.getSource().visit(this);
        builder.append("):");
        statement.getChild().visit(this);
    }

    @Override
    public void handleWhileLoop(@NotNull WhileLoopStatement statement) {
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
    public void handleBreakContinue(@NotNull BreakContinueStatement statement) {
        builder.append(statement.isContinue() ? "CONTINUE" : "BREAK");
    }

    @Override
    public void handleNullLiteral(@NotNull NullExpression literal) {
        builder.append("NULL");
    }

    @Override
    public void handleBooleanLiteral(@NotNull BooleanExpression literal) {
        builder.append(literal.getRaw());
    }

    @Override
    public void handleNumberLiteral(@NotNull NumberExpression literal) {
        double num = literal.getRaw();
        builder.append(formatNumber.format(num));
    }

    @Override
    public void handleStringLiteral(@NotNull StringExpression literal) {
        builder.append("\"").append(literal.getRaw()).append("\"");
    }

    @Override
    public void handleEntityTypeLiteral(@NotNull EntityTypeExpression literal) {
        builder.append("EntityType.").append(literal.getRaw());
    }

    @Override
    public void handleRuntimeLiteral(@NotNull RuntimeLiteral literal) {
        builder.append("'").append(literal.getRaw()).append("'");
    }

    @Override
    public void handleDurationLiteral(@NotNull DurationExpression literal) {
        Duration duration = literal.getRaw();
        builder.append(formatNumber.format(duration.amount()))
                .append(" ")
                .append(duration.niceUnit());
    }

    @Override
    public void handleLocationLiteral(@NotNull LocationLiteral literal) {
        builder.append("Location(");
        literal.getWorld().visit(this); builder.append(", ");
        literal.getVectorX().visit(this); builder.append(", ");
        literal.getVectorY().visit(this); builder.append(", ");
        literal.getVectorZ().visit(this);
        if(literal.asYawAndPitch()) {
            builder.append(", ");
            literal.getYaw().visit(this);
            builder.append(", ");
            literal.getPitch().visit(this);
        }
        builder.append(")");
    }

    @Override
    public void handleBiOperator(@NotNull BiOperator operator) {
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
            case LIST_ADD -> "append";
            case LIST_REM -> "remove";
            case LIST_CONTAINS -> "contains";
            case LIST_REM_INDEX -> "remove_idx";
        };
        builder.append(" ").append(ope).append(" ");
        operator.getRight().visit(this);
    }

    @Override
    public void handleMonoOperator(@NotNull MonoOperator operator) {
        String ope = operator.getType().name();
        builder.append(ope).append("(");
        operator.getChild().visit(this);
        builder.append(")");
    }

    @Override
    public void handleParenthesis(@NotNull ParenthesisExpression parenthesis) {
        builder.append("(");
        parenthesis.getExpression().visit(this);
        builder.append(")");
    }

    @Override
    public void handleArrayGet(@NotNull ArrayGetterExpression arrayGetter) {
        arrayGetter.getArray().visit(this);
        builder.append("[");
        arrayGetter.getIndex().visit(this);
        builder.append("]");
    }

    @Override
    public void handlePropertiesSet(@NotNull PropertiesExpression expression) {
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
    public void handleAllAround(@NotNull AllEntitiesAroundExpression expression) {
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
    public void handlePositionOf(@NotNull PositionOfExpression expression) {
        builder.append("position of (");
        expression.getEntity().visit(this);
        builder.append(")");
    }

    @Override
    public void handleSizeOf(@NotNull SizeOfExpression expression) {
        builder.append("sizeof(");
        expression.getChild().visit(this);
        builder.append(")");
    }

    @Override
    public void handleFunction(@NotNull FunctionCallExpression expression) {
        builder.append(":")
                .append(expression.getFunction().id())
                .append("(");
        boolean first = true;
        for(ExpressionNode arg : expression.getArguments()) {
            if(first) first = false; else builder.append(", ");
            arg.visit(this);
        }
        builder.append(")");
    }

    @Override
    public void handleArray(@NotNull ArrayExpression expression) {
        builder.append("[");
        boolean first = true;
        for(ExpressionNode child : expression.getElements()) {
            if(first) first = false; else builder.append(", ");
            child.visit(this);
        }
        builder.append("]");
    }

    @Override
    public void handleVariable(@NotNull VariableExpression expression) {
        builder.append("%").append(expression.getVariableName());
    }
}
