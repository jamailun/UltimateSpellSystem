package fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.StatementVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * {@code if} statement. Can be linked to a ELSE.
 */
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

    /**
     * Parse a new if+else statement.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
    @PreviousIndicator(expected = TokenType.IF)
    public static @NotNull IfElseStatement parseIfStatement(@NotNull TokenStream tokens) {
        // Condition
        tokens.dropOrThrow(TokenType.BRACKET_OPEN, "This is not Python, a '(' is required after the IF keyword.");
        ExpressionNode condition = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.BRACKET_CLOSE, "A ')' is expected after the IF condition.");

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
