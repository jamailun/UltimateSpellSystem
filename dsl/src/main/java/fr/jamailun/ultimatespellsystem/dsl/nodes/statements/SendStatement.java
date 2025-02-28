package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public abstract class SendStatement extends StatementNode {

    protected final ExpressionNode target;

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        assertExpressionType(target, context, TypePrimitive.ENTITY);
    }

    @PreviousIndicator(expected = {TokenType.SEND}) // SEND (to) TARGET <THING> (...)
    public static @NotNull StatementNode parseSendStatement(@NotNull TokenStream tokens) {
        // Optional "to"
        tokens.dropOptional(TokenType.TO);

        // Read target expression
        ExpressionNode target = ExpressionNode.readNextExpression(tokens);

        // Type of send
        Token token = tokens.next();
        String type = token.getContentString();
        if(type == null)
            throw new SyntaxException(token, "Invalid SEND type. Expected a string-value container.");
        return switch (type.toLowerCase()) {
            case "message" -> SendMessageStatement.parseSendMessage(target, tokens);
            case "effect" -> SendEffectStatement.parseSendEffect(target, tokens);
            case "attribute", "attributes" -> SendAttributeStatement.parseAttributeEffect(target, tokens);
            default -> throw new SyntaxException(token, "Expected SEND type (message, effect).");
        };
    }

}
