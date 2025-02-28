package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

public abstract class BiOperator extends Operator {

    protected Type producedType = TypePrimitive.NULL.asType();

    protected final ExpressionNode left;
    protected final ExpressionNode right;

    protected BiOperator(TokenPosition position, ExpressionNode left, ExpressionNode right) {
        super(position);
        this.left = left;
        this.right = right;
    }

    public abstract @NotNull BiOpeType getType();

    @Override
    public @NotNull Type getExpressionType() {
        return producedType;
    }

    @Override
    public final void validateTypes(@NotNull TypesContext context) {
        // Validate
        left.validateTypes(context);
        right.validateTypes(context);

        // Assert
        Type leftType = left.getExpressionType();
        Type rightType = right.getExpressionType();

        validateTypes(leftType, rightType);
    }

    protected abstract void validateTypes(@NotNull Type leftType, @NotNull Type rightType);

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleBiOperator(this);
    }

    public static BiOperator parseBiOperator(ExpressionNode left, Token operand, ExpressionNode right) {
        TokenPosition pos = operand.pos();
        return switch (operand.getType()) {
            case OPE_ADD -> new AddOperator(pos, left, right);
            case OPE_SUB -> new SubOperator(pos, left, right);
            case OPE_MUL, OPE_DIV -> new MulDivOperator(operand, left, right);
            case COMP_EQ, COMP_NE,
                    COMP_GE, COMP_GT,
                    COMP_LE, COMP_LT,
                    OPE_AND, OPE_OR -> new LogicalOperator(operand, left, right);
            default -> throw new SyntaxException(operand, "Unknown Bi-operator.");
        };
    }

    public @NotNull ExpressionNode getLeft() {
        return left;
    }

    public @NotNull ExpressionNode getRight() {
        return right;
    }

    public enum BiOpeType {

        // Math

        ADD,
        SUB,
        MUL,
        DIV,

        // Logical

        EQUAL,
        NOT_EQUAL,
        GREATER_OR_EQ,
        GREATER,
        LESSER_OR_EQ,
        LESSER,

        AND,
        OR
    }

}
