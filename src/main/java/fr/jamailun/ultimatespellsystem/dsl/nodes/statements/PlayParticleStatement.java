package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

import java.util.Optional;

public class PlayParticleStatement extends PlayStatement {

    private final String particleType;
    private final ExpressionNode optProperties;

    public PlayParticleStatement(String particleType, ExpressionNode location, ExpressionNode optProperties) {
        super(location);
        this.particleType = particleType;
        this.optProperties = optProperties;
    }

    @Override
    public void validateTypes(TypesContext context) {
        // Standard properties
        if(optProperties != null) {
            assertExpressionType(optProperties, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.PROPERTIES_SET);
        }
    }

    @Override
    public void visit(StatementVisitor visitor) {

    }

    @PreviousIndicator(expected = TokenType.PARTICLE) // PLAY PARTICLE <type> AT <loc> [WITH (opt)]
    public static PlayParticleStatement parsePlayParticle(TokenStream tokens) {
        Token type = tokens.nextOrThrow(TokenType.IDENTIFIER);
        tokens.dropOrThrow(TokenType.AT);
        ExpressionNode loc = ExpressionNode.readNextExpression(tokens);
        ExpressionNode properties = null;
        if(tokens.dropOptional(TokenType.WITH)) {
            properties = ExpressionNode.readNextExpression(tokens);
        }
        return new PlayParticleStatement(type.getContentString(), loc, properties);
    }

    public String getParticleType() {
        return particleType;
    }

    public Optional<ExpressionNode> getProperties() {
        return Optional.ofNullable(optProperties);
    }
}
