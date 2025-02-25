package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.NumberExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A give-statement.
 */
@Getter
@RequiredArgsConstructor
public class GiveStatement extends StatementNode {

    private final @NotNull ExpressionNode target;
    private final @Nullable ExpressionNode optAmount;
    private final @Nullable ExpressionNode optType;
    private final @Nullable ExpressionNode optProperties;

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleGive(this);
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        assertExpressionType(target, context, TypePrimitive.ENTITY);
        if(optAmount != null)
            assertExpressionType(optAmount, context, TypePrimitive.NUMBER);
        if(optType != null)
            assertExpressionType(optType, context, TypePrimitive.STRING, TypePrimitive.CUSTOM);
        if(optProperties != null)
            assertExpressionType(optProperties, context, TypePrimitive.PROPERTIES_SET);
    }

    @Override
    public String toString() {
        return "GIVE{"
                + (getOptAmount()==null?"":getOptAmount()+" ")
                + (getOptType()==null?"":getOptType()+" ")
                + "TO " + getTarget()
                + (getOptProperties()==null?"":" WITH: " + getOptProperties())+"}";
    }

    @PreviousIndicator(expected = TokenType.GIVE) // GIVE [[<AMOUNT>] <TYPE>] TO <TARGET> [WITH: <PROPERTIES>]
    public static @NotNull GiveStatement parseNextGiveStatement(@NotNull TokenStream tokens) {
        // Amount + type. both are optional, but 'amount' is even more optional.
        ExpressionNode amount, type;
        if(tokens.dropOptional(TokenType.TO)) {
            // No amount + type
            amount = null;
            type = null;
        } else {
            ExpressionNode node = ExpressionNode.readNextExpression(tokens, true);
            if(tokens.dropOptional(TokenType.TO)) {
                type = node;
                amount = new NumberExpression(tokens.position(), 1);
            } else {
                amount = node;
                type = ExpressionNode.readNextExpression(tokens, true);
                tokens.dropOrThrow(TokenType.TO);
            }
        }

        // Now, target
        ExpressionNode target = ExpressionNode.readNextExpression(tokens);

        // With ?
        ExpressionNode properties;
        if(tokens.dropOptional(TokenType.WITH)) {
            tokens.dropOrThrow(TokenType.COLON);
            properties = ExpressionNode.readNextExpression(tokens);
        } else {
            properties = null;
        }

        tokens.dropOptional(TokenType.SEMI_COLON);

        // create
        return new GiveStatement(target, amount, type, properties);
    }
}
