package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class PlayStatement extends StatementNode {

    private final Type type;
    private final ExpressionNode location, properties;

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        assertExpressionType(location, context, TypePrimitive.LOCATION, TypePrimitive.ENTITY);
        assertExpressionType(properties, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.PROPERTIES_SET);
    }

    @PreviousIndicator(expected = TokenType.PLAY) // PLAY <BLOCK/PARTICLE/SOUND> AT <LOC> WITH: <DATA>;
    public static @NotNull PlayStatement parsePlay(@NotNull TokenStream tokens) {
        Type type = readType(tokens);
        tokens.dropOrThrow(TokenType.AT);
        ExpressionNode location = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.WITH);
        tokens.dropOptional(TokenType.COLON);
        ExpressionNode properties = ExpressionNode.readNextExpression(tokens);
        tokens.dropOptional(TokenType.SEMI_COLON);
        return new PlayStatement(type, location, properties);
    }

    private static @NotNull Type readType(@NotNull TokenStream tokens) {
        Token next = tokens.next();
        String nextValue = next.getContentString();
        if(nextValue == null)
            throw new SyntaxException(next, "Expected a token with a string value (STRING, IDENTIFIER)");
        return switch(nextValue.toLowerCase()) {
            case "block" -> Type.BLOCK;
            case "particle" -> Type.PARTICLE;
            case "sound" -> Type.SOUND;
            default -> throw new SyntaxException(next, "Expected either 'block', 'particle' or 'sound' after a 'PLAY' statement.");
        };
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handlePlay(this);
    }

    public enum Type {
        BLOCK, PARTICLE, SOUND
    }

    @Override
    public String toString() {
        return "Play_"+type+" :: " + properties;
    }
}
