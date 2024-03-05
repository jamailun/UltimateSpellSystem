package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import org.apache.commons.lang3.NotImplementedException;

public abstract class PlayStatement extends StatementNode {

    private final ExpressionNode location;

    public PlayStatement(ExpressionNode location) {
        this.location = location;
    }

    @Override
    public void validateTypes(TypesContext context) {
        // Location can be an array, and can be an entity.
        assertExpressionType(location, context, TypePrimitive.LOCATION, TypePrimitive.ENTITY);
    }

    @PreviousIndicator(expected = TokenType.PLAY) // PLAY PARTICLE/BLOCK
    public static PlayStatement parsePlay(TokenStream tokens) {
        Token next = tokens.next();
        return switch(next.getType()) {
            case PARTICLE -> PlayParticleStatement.parsePlayParticle(tokens);
            case BLOCK -> PlayBlockStatement.parsePlayBlock(tokens);
            default -> throw new SyntaxException(next, "Expected either 'PARTICLE' or 'BLOCK' after a 'PLAYER'.");
        };
    }

    public ExpressionNode getLocation() {
        return location;
    }

}
