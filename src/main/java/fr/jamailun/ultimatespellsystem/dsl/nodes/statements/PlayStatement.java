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

@Getter
public class PlayStatement extends StatementNode {

    private final Type type;
    private final ExpressionNode location, properties;

    public PlayStatement(Type type, ExpressionNode location, ExpressionNode properties) {
        this.type = type;
        this.location = location;
        this.properties = properties;
    }

    @Override
    public void validateTypes(TypesContext context) {
        assertExpressionType(location, context, TypePrimitive.LOCATION, TypePrimitive.ENTITY);
        assertExpressionType(properties, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.PROPERTIES_SET);
    }

    @PreviousIndicator(expected = TokenType.PLAY) // PLAY PARTICLE/BLOCK
    public static PlayStatement parsePlay(TokenStream tokens) {
        Token next = tokens.next();
        Type type = switch(next.getType()) {
            case PARTICLE -> Type.PARTICLE;
            case BLOCK -> Type.BLOCK;
            case SOUND -> Type.SOUND;
            default -> throw new SyntaxException(next, "Expected either 'PARTICLE' or 'BLOCK' after a 'PLAYER'.");
        };
        tokens.dropOrThrow(TokenType.AT);
        ExpressionNode location = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.WITH);
        tokens.dropOptional(TokenType.COLON);
        ExpressionNode properties = ExpressionNode.readNextExpression(tokens);
        tokens.dropOptional(TokenType.SEMI_COLON);
        return new PlayStatement(type, location, properties);
    }

    @Override
    public void visit(StatementVisitor visitor) {
        visitor.handlePlay(this);
    }

    public enum Type {
        BLOCK, PARTICLE, SOUND
    }

}
