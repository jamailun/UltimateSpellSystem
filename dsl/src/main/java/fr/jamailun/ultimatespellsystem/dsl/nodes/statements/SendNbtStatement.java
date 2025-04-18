package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Send a NBT k/v to a Bukkit entity.
 */
public class SendNbtStatement extends SendStatement {

    @Getter private final ExpressionNode nbtName;
    @Getter private final ExpressionNode nbtValue;
    @Getter private final ExpressionNode nbtDuration;

    public SendNbtStatement(ExpressionNode target, ExpressionNode nbtName, ExpressionNode nbtValue, ExpressionNode nbtDuration) {
        super(target);
        this.nbtName = nbtName;
        this.nbtValue = nbtValue;
        this.nbtDuration = nbtDuration;
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        super.validateTypes(context);

        assertExpressionType(nbtName, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.STRING, TypePrimitive.CUSTOM);
        assertExpressionType(nbtDuration, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.DURATION);
        nbtValue.validateTypes(context.childContext());
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleSendNbt(this);
    }

    /**
     * Parse a send-nbt statement. Called by {@link SendStatement#parseSendStatement(TokenStream)}.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
    @PreviousIndicator(expected = {TokenType.SEND/* + NBT */}) // <NAME> = <VALUE> for <DURATION>;
    public static @NotNull SendNbtStatement parseSendNbt(@NotNull ExpressionNode target, @NotNull TokenStream tokens) {
        ExpressionNode nbtName = ExpressionNode.readNextExpression(tokens, true);
        tokens.dropOrThrow(TokenType.EQUAL);
        ExpressionNode nbtValue = ExpressionNode.readNextExpression(tokens, true);
        tokens.dropOrThrow(TokenType.FOR);
        ExpressionNode nbtDuration = ExpressionNode.readNextExpression(tokens);

        // Optional EOL
        tokens.dropOptional(TokenType.SEMI_COLON);

        // return
        return new SendNbtStatement(target, nbtName, nbtValue, nbtDuration);
    }

    @Override
    public String toString() {
        return "SEND_NBT{to="+target+", name=" + nbtName + ", val=" + nbtValue + ", for=" + nbtDuration + "}";
    }
}
