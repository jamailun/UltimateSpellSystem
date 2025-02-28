package fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class IfElseStatement extends BlockHolder {

    @Getter private final ExpressionNode condition;
    private final StatementNode optElse;

    public IfElseStatement(@NotNull ExpressionNode condition, @NotNull StatementNode child, @Nullable StatementNode elseStatement) {
        super(child);
        this.condition = condition;
        this.optElse = elseStatement;
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        assertExpressionType(condition, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.BOOLEAN);
        child.validateTypes(context.childContext());
        if(optElse != null)
            optElse.validateTypes(context);
    }

    public Optional<StatementNode> getElse() {
        return Optional.ofNullable(optElse);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleIf(this);
    }

    @PreviousIndicator(expected = TokenType.IF)
    public static @NotNull IfElseStatement parseIfStatement(@NotNull TokenStream tokens) {
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
                + (optElse==null ? "" : "\n  ELSE : " + optElse);
    }
}
