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
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Send a bukkit Attribute to a Bukkit Entity.
 */
@Getter
public class SendAttributeStatement extends SendStatement {

    private final ExpressionNode numericValue;
    private final ExpressionNode attributeType;
    private final @Nullable ExpressionNode attributeMode;
    private final ExpressionNode duration;

    public SendAttributeStatement(ExpressionNode target, ExpressionNode numericValue, ExpressionNode attributeType, @Nullable ExpressionNode attributeMode, ExpressionNode duration) {
        super(target);
        this.numericValue = numericValue;
        this.attributeType = attributeType;
        this.attributeMode = attributeMode;
        this.duration = duration;
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        super.validateTypes(context);

        assertExpressionType(numericValue, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.NUMBER);
        assertExpressionType(attributeType, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.CUSTOM, TypePrimitive.STRING);
        assertExpressionType(duration, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.DURATION);
        if(attributeMode != null)
            assertExpressionType(attributeMode, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.CUSTOM, TypePrimitive.STRING);
    }

    public Optional<ExpressionNode> getAttributeMode() {
        return Optional.ofNullable(attributeMode);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleSendAttribute(this);
    }

    /**
     * Parse a send-attribute statement. Called by {@link SendStatement#parseSendStatement(TokenStream)}.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
    @PreviousIndicator(expected = {TokenType.SEND/* + ATTRIBUTE */})
    public static @NotNull SendAttributeStatement parseAttributeEffect(@NotNull ExpressionNode target, @NotNull TokenStream tokens) {
        // Value
        ExpressionNode numericValue = ExpressionNode.readNextExpression(tokens);

        // Type
        ExpressionNode attributeType = ExpressionNode.readNextExpression(tokens, true);

        // Syntax: "[mode] for <dur>"
        ExpressionNode mode = null;
        if( ! tokens.dropOptional(TokenType.FOR)) {
            mode = ExpressionNode.readNextExpression(tokens, true);
            tokens.dropOrThrow(TokenType.FOR);
        }

        ExpressionNode duration = ExpressionNode.readNextExpression(tokens);

        // Optional EOL
        tokens.dropOptional(TokenType.SEMI_COLON);

        // return
        return new SendAttributeStatement(target, numericValue, attributeType, mode, duration);
    }

    @Override
    public String toString() {
        return "SEND_ATTRIBUTE{to="+target+", attr=" + numericValue + " " + attributeType + (attributeMode==null?"":" ("+attributeMode + ")") + ", for=" + duration + "}";
    }
}
