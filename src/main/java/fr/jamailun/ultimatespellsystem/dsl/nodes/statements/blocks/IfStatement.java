package fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

import java.util.Optional;

public class IfStatement extends BlockHolder {

    private final ExpressionNode condition;
    private final ElseStatement optElse;

    public IfStatement(ExpressionNode condition, StatementNode child, ElseStatement elseStatement) {
        super(child);
        this.condition = condition;
        this.optElse = elseStatement;
    }

    @Override
    public void validateTypes(TypesContext context) {
        assertExpressionType(condition, context, TypePrimitive.BOOLEAN);
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public Optional<ElseStatement> getElse() {
        return Optional.ofNullable(optElse);
    }

    @Override
    public void visit(StatementVisitor visitor) {
        visitor.handleIf(this);
    }

    @PreviousIndicator(expected = TokenType.IF)
    public static IfStatement parseIfStatement(TokenStream tokens) {
        // Condition
        tokens.dropOrThrow(TokenType.BRACKET_OPEN);
        ExpressionNode condition = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.BRACKET_CLOSE);

        // Content
        StatementNode child = StatementNode.parseNextStatement(tokens);

        ElseStatement elseStatement = null;
        if(tokens.dropOptional(TokenType.ELSE)) {
            elseStatement = ElseStatement.parseElseStatement(tokens);
        }

        // Return
        return new IfStatement(condition, child, elseStatement);
    }

    @Override
    public String toString() {
        return "IF(" + condition +") : " + child
                + (optElse==null ? "" : "\n " + optElse);
    }
}
