package fr.jamailun.ultimatespellsystem.dsl2.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.StatementVisitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Set a variable value.
 */
@Getter
@RequiredArgsConstructor
public class AffectationStatement extends StatementNode {

    private final ExpressionNode valueHolder;
    private final ExpressionNode expression;

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        //TODO
        // expression.validateTypes(context);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleAffectVariable(this);
    }

    @Override
    public @NotNull String toString() {
        return valueHolder + " = " + expression;
    }

    /**
     * Parse a define statement.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
    public static @NotNull StatementNode parseNextDefine(@NotNull ExpressionNode affected, @NotNull TokenStream tokens) {
        ExpressionNode expression = ExpressionNode.readNextExpression(tokens);

        // optional ;
        tokens.dropOptional(TokenType.SEMI_COLON);

        return new AffectationStatement(affected, expression);
    }
}
