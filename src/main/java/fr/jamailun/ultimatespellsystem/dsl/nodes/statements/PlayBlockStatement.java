package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

import java.util.Optional;

public class PlayBlockStatement extends PlayStatement {

    private final ExpressionNode blockType;
    private final ExpressionNode optProperties;

    public PlayBlockStatement(ExpressionNode location, ExpressionNode blockType, ExpressionNode properties) {
        super(location);
        this.blockType = blockType;
        this.optProperties = properties;
    }

    @Override
    public void validateTypes(TypesContext context) {
        assertExpressionType(blockType, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.MATERIAL);
        // Standard properties
        if(optProperties != null) {
            assertExpressionType(optProperties, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.PROPERTIES_SET);
        }
    }

    @Override
    public void visit(StatementVisitor visitor) {

    }

    @PreviousIndicator(expected = TokenType.PARTICLE) // PLAY BLOCK <type> AT <loc> [WITH (opt)]
    public static PlayBlockStatement parsePlayBlock(TokenStream tokens) {
        ExpressionNode blockType = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.AT);
        ExpressionNode loc = ExpressionNode.readNextExpression(tokens);
        ExpressionNode properties = null;
        if(tokens.dropOptional(TokenType.WITH)) {
            properties = ExpressionNode.readNextExpression(tokens);
        }
        return new PlayBlockStatement(blockType, loc, properties);
    }

    public ExpressionNode getBlockType() {
        return blockType;
    }

    public Optional<ExpressionNode> getProperties() {
        return Optional.ofNullable(optProperties);
    }
}
