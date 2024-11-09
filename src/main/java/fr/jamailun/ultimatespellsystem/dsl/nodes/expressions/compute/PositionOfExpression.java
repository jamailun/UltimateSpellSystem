package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.compute;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

public class PositionOfExpression extends ExpressionNode {

    private final ExpressionNode entityNode;
    private boolean isCollection = false;

    public PositionOfExpression(TokenPosition pos, ExpressionNode entityNode) {
        super(pos);
        this.entityNode = entityNode;
    }

    public ExpressionNode getEntity() {
        return entityNode;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return TypePrimitive.LOCATION.asType(isCollection);
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handlePositionOf(this);
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        assertExpressionType(entityNode, context, TypePrimitive.ENTITY);
        isCollection = entityNode.getExpressionType().isCollection();
    }

    @PreviousIndicator(expected = TokenType.POSITION)
    public static @NotNull PositionOfExpression parsePositionOf(@NotNull TokenStream tokens) {
        TokenPosition pos = tokens.position();
        tokens.dropOrThrow(TokenType.OF);
        ExpressionNode entityNode = ExpressionNode.readNextExpression(tokens);
        return new PositionOfExpression(pos, entityNode);
    }
}
