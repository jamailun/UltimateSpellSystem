package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl2.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl2.library.StructDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.ExpressionVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Get array value at index.
 */
@Getter
public class FieldGetExpression extends ExpressionNode {

    private final ExpressionNode leftExpression;
    private final String fieldName;

    private StructDefinition leftStruct;

    public FieldGetExpression(@NotNull ExpressionNode leftExpression, @NotNull String fieldName) {
        super(leftExpression.firstTokenPosition());
        this.leftExpression = leftExpression;
        this.fieldName = fieldName;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return leftStruct.asType();
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        // 1. Valider le holder, récupérer son type.
        leftExpression.validateTypes(context);
        Type type = leftExpression.getExpressionType();

        // 2. Trouver la structure qui correspond au type
        StructDefinition leftStruct = context.findStruct(type);
        if(leftStruct == null) {
            throw new TypeException(leftExpression.firstTokenPosition(), "Type value " + leftExpression + " of type " + type + " as not structure referenced. As such, cannot get field '" + fieldName + "' on it.");
        }
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleFieldGet(this);
    }

    @Override
    public String toString() {
        return firstTokenPosition() + "." + fieldName;
    }
}
