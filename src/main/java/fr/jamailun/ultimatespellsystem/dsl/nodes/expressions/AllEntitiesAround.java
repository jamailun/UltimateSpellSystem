package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;

public class AllEntitiesAround extends ExpressionNode {

    private final ExpressionNode entityType;
    private final ExpressionNode source;
    private final ExpressionNode distance;
    private final boolean including;

    protected AllEntitiesAround(TokenPosition pos, ExpressionNode entityType, boolean including, ExpressionNode source, ExpressionNode distance) {
        super(pos);
        this.entityType = entityType;
        this.including = including;
        this.source = source;
        this.distance = distance;
    }

    @Override
    public Type getExpressionType() {
        return new Type(TypePrimitive.ENTITY, true);
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
        visitor.handleFunction(this);
    }

    @Override
    public void validateTypes(TypesContext context) {
        assertExpressionType(entityType, TypePrimitive.CUSTOM, context);
        assertExpressionType(source, TypePrimitive.ENTITY, context);
        assertExpressionType(distance, TypePrimitive.NUMBER, context);
    }

    @Override
    public String toString() {
        return "FETCH_ALL{" + entityType + " around " + source + (including ?"(STRICT)":"") + ", within "+ distance + "}";
    }

    @PreviousIndicator(expected = {TokenType.ALL}) // all (ENTITY_TYPE) within (DISTANCE) around (SOURCE) [[including]]
    public static AllEntitiesAround parseAllExpression(TokenStream tokens) {
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

        return new AllEntitiesAround(pos, scope, including, source, distance);
    }
}
