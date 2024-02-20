package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.Visitor;

public class SendMessageStatement extends SendStatement {

    protected final ExpressionNode message;

    public SendMessageStatement(ExpressionNode target, ExpressionNode message) {
        super(target);
        this.message = message;
    }

    @Override
    public void validateTypes(TypesContext context) {
        super.validateTypes(context);
        assertExpressionType(message, TypePrimitive.STRING, context);
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.handleSendMessage(this);
    }

    @PreviousIndicator(expected = {TokenType.MESSAGE})
    public static SendMessageStatement parseSendMessage(ExpressionNode target, TokenStream tokens) {
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
