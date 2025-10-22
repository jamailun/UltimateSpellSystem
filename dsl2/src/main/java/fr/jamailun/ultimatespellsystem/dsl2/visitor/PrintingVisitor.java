package fr.jamailun.ultimatespellsystem.dsl2.visitor;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionCallExpression;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators.BiOperator;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators.MonoOperator;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.ForLoopStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.IfElseStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.WhileLoopStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Duration;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

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
    public void handleReturn(@NotNull ReturnStatement statement) {
        builder.append(indent()).append("stop");
    }

    @Override
    public void handleDeclareVariable(@NotNull DeclareNewVariableStatement statement) {
        builder.append(indent())
                .append("define %")
                .append(statement.getVarName());

        if(statement.getExpression() != null) {
            builder.append(" = ");
            statement.getExpression().visit(this);
        }
    }

    @Override
    public void handleAffectVariable(@NotNull AffectationStatement statement) {
        statement.getValueHolder().visit(this);
        builder.append(" := ");
        statement.getExpression().visit(this);
    }

    @Override
    public void handleFunctionDeclaration(@NotNull FunctionDeclarationStatement statement) {
        builder.append("FUNCTION ")
            .append(statement.getFunctionReturnType())
            .append(" ")
            .append(statement.getFunctionName())
            .append("(");
        boolean first = true;
        for(var arg : statement.getParameters()) {
            if(first) first = false; else builder.append(", ");
            builder.append(arg);
        }
        builder.append(") {\n");
        right();
        for(var st : statement.getStatements()) {
            st.visit(this);
            eol();
        }
        left();
        builder.append("}");
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
    public void handleSimpleExpression(@NotNull SimpleExpressionStatement statement) {
        builder.append("{");
        statement.getChild().visit(this);
        builder.append("}");
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
    public void handleMapLiteral(@NotNull MapLiteral literal) {
        //TODO
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
    public void handleFieldGet(@NotNull FieldGetExpression fieldGetter) {
        fieldGetter.getLeftExpression().visit(this);
        builder.append(".")
            .append(fieldGetter.getFieldName());
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
    public void handleVariable(@NotNull ReferenceExpression expression) {
        builder.append("VAR<")
                .append(expression.getVariableName())
                .append(">");
    }

}
