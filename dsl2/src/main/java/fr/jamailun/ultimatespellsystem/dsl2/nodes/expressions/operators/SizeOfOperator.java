package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators;


import fr.jamailun.ultimatespellsystem.dsl2.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Size of a collection.
 */
@Getter
public class SizeOfOperator extends MonoOperator {

    protected SizeOfOperator(TokenPosition position, ExpressionNode child) {
        super(position, child);
    }

    @Override
    public void validateTypes(@NotNull Type childType) {
        // Just needs to be a collection !
        if( ! childType.isCollection()) {
            throw new TypeException(this, "A SIZE_OF can only handle collections.");
        }
    }

    @Override
    public @NotNull MonoOpeType getType() {
        return MonoOpeType.SIZE_OF;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return TypePrimitive.NUMBER.asType();
    }

    @PreviousIndicator(expected = TokenType.SIZE_OF)
    public static @NotNull SizeOfOperator parseSizeOf(@NotNull TokenStream tokens) {
        TokenPosition position = tokens.position();
        boolean parenthesis = tokens.dropOptional(TokenType.BRACKET_OPEN);

        ExpressionNode child = ExpressionNode.readNextExpression(tokens);

        if(parenthesis)
            tokens.dropOrThrow(TokenType.BRACKET_CLOSE, "Need a parenthesis to close the size-of operator.");

        return new SizeOfOperator(position, child);
    }

    @Override
    public String toString() {
        return "size_of(" + child + ")";
    }
}
