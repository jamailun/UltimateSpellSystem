package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class SendMessageStatement extends SendStatement {

    protected final ExpressionNode message;

    public SendMessageStatement(@NotNull ExpressionNode target, @NotNull ExpressionNode message) {
        super(target);
        this.message = message;
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        super.validateTypes(context);
        assertExpressionType(message, context, TypePrimitive.STRING);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleSendMessage(this);
    }

    @PreviousIndicator(expected = {TokenType.SEND/* + MESSAGE */})
    public static @NotNull SendMessageStatement parseSendMessage(@NotNull ExpressionNode target, @NotNull TokenStream tokens) {
        ExpressionNode message = ExpressionNode.readNextExpression(tokens);

        // Optional EOL
        tokens.dropOptional(TokenType.SEMI_COLON);

        return new SendMessageStatement(target, message);
    }

    @Override
    public String toString() {
        return "SEND_MSG{to="+target+", " + message + "}";
    }
}
