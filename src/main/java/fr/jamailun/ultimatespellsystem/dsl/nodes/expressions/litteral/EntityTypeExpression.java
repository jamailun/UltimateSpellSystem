package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;

public class EntityTypeExpression extends LiteralExpression<String> {

    private final String type;

    public EntityTypeExpression(TokenPosition position, String type) {
        super(position);
        this.type = type;
    }

    @Override
    public String getRaw() {
        return type;
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.ENTITY_TYPE.asType();
    }

    @Override
    public String toString() {
        return PREFIX + "EntityType." + type + SUFFIX;
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
        visitor.handleEntityTypeLiteral(this);
    }

}
