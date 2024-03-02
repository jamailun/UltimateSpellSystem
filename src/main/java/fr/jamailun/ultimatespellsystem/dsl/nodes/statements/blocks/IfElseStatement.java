package fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

import java.util.Optional;

public class IfElseStatement extends BlockHolder {

    private final ExpressionNode condition;
    private final StatementNode optElse;

    public IfElseStatement(ExpressionNode condition, StatementNode child, StatementNode elseStatement) {
        super(child);
        this.condition = condition;
        this.optElse = elseStatement;
    }

    @Override
    public void validateTypes(TypesContext context) {
        assertExpressionType(condition, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.BOOLEAN);
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public Optional<StatementNode> getElse() {
        return Optional.ofNullable(optElse);
    }

    @Override
    public void visit(StatementVisitor visitor) {
        visitor.handleIf(this);
    }

    @PreviousIndicator(expected = TokenType.IF)
    public static IfElseStatement parseIfStatement(TokenStream tokens) {
        // Condition
        tokens.dropOrThrow(TokenType.BRACKET_OPEN);
        ExpressionNode condition = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.BRACKET_CLOSE);

        // Content
        StatementNode child = StatementNode.parseNextStatement(tokens);

        StatementNode elseStatement = null;
        if(tokens.dropOptional(TokenType.ELSE)) {
            elseStatement = StatementNode.parseNextStatement(tokens);
        }

        // Return
        return new IfElseStatement(condition, child, elseStatement);
    }

    @Override
    public String toString() {
        return "IF(" + condition +") : " + child
                + (optElse==null ? "" : "\n " + optElse);
    }
}
