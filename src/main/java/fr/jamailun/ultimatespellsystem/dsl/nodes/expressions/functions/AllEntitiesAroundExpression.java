package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

public class AllEntitiesAroundExpression extends ExpressionNode {

    private final ExpressionNode entityType;
    private final ExpressionNode source;
    private final ExpressionNode distance;
    private final boolean including;

    protected AllEntitiesAroundExpression(TokenPosition pos, ExpressionNode entityType, boolean including, ExpressionNode source, ExpressionNode distance) {
        super(pos);
        this.entityType = entityType;
        this.including = including;
        this.source = source;
        this.distance = distance;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return new Type(TypePrimitive.ENTITY, true);
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleAllAround(this);
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        // Distance must be a number
        assertExpressionType(distance, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.NUMBER);

        // Scope can be entity-type OR custom
        assertExpressionType(entityType, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.ENTITY_TYPE, TypePrimitive.CUSTOM);

        // Source can be entity OR location
        assertExpressionType(source, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.ENTITY, TypePrimitive.LOCATION);
    }

    @Override
    public String toString() {
        return "FETCH_ALL{" + entityType + " around " + source + (including ?"(STRICT)":"") + ", within "+ distance + "}";
    }

    @PreviousIndicator(expected = {TokenType.ALL}) // all (SCOPE) within (DISTANCE) around (SOURCE) [[including]]
    public static AllEntitiesAroundExpression parseAllExpression(TokenStream tokens) {
        TokenPosition pos = tokens.position();

        // Entity type
        ExpressionNode scope = ExpressionNode.readNextExpression(tokens, true);

        //
        tokens.dropOrThrow(TokenType.WITHIN);
        ExpressionNode distance = ExpressionNode.readNextExpression(tokens);

        // DistanceSource
        tokens.dropOrThrow(TokenType.AROUND);
        ExpressionNode source = ExpressionNode.readNextExpression(tokens);
        boolean including = tokens.dropOptional(TokenType.INCLUDING);

        return new AllEntitiesAroundExpression(pos, scope, including, source, distance);
    }

    public ExpressionNode getEntityType() {
        return entityType;
    }

    public ExpressionNode getSource() {
        return source;
    }

    public ExpressionNode getDistance() {
        return distance;
    }

    public boolean isIncluding() {
        return including;
    }
}
