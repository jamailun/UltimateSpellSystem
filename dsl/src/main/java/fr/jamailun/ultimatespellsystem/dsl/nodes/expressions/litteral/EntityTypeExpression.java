package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * A raw {@link org.bukkit.entity.EntityType} literal.
 */
public class EntityTypeExpression extends LiteralExpression<String> {

    private final String type;

    /**
     * New instance.
     * @param position token position.
     * @param type entity-type value.
     */
    public EntityTypeExpression(@NotNull TokenPosition position, @NotNull String type) {
        super(position);
        this.type = type;
    }

    @Override
    public String getRaw() {
        return type;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return TypePrimitive.ENTITY_TYPE.asType();
    }

    @Override
    public String toString() {
        return PREFIX + "EntityType." + type + SUFFIX;
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleEntityTypeLiteral(this);
    }

}
