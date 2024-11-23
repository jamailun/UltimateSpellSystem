package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Define statement will set a variable to something.
 */
@Getter
@RequiredArgsConstructor
public class DefineStatement extends StatementNode {

    private final String varName;
    private final ExpressionNode expression;

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        expression.validateTypes(context);
        // Register variable
        context.registerVariable(varName, expression);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleDefine(this);
    }

    @Override
    public String toString() {
        return "DEFINE{%" + varName + " <- " + expression + "}";
    }

    @PreviousIndicator(expected = {TokenType.DEFINE})
    public static @NotNull DefineStatement parseNextDefine(@NotNull TokenStream tokens) {
        // %VAR_NAME
        Token varToken = tokens.nextOrThrow(TokenType.VALUE_VARIABLE);
        String varName = varToken.getContentString();

        // =
        tokens.dropOrThrow(TokenType.EQUAL);

        ExpressionNode expression = ExpressionNode.readNextExpression(tokens);

        // optional ;
        tokens.dropOptional(TokenType.SEMI_COLON);

        return new DefineStatement(varName, expression);
    }
}
