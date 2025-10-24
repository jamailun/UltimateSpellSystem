package fr.jamailun.ultimatespellsystem.dsl2.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl2.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.ReferenceExpression;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.StatementVisitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Set a variable value.
 */
@Getter
@RequiredArgsConstructor
public class AffectationStatement extends StatementNode {

    private final ExpressionNode valueHolder;
    private final ExpressionNode expression;

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        // Validate right expression
        expression.validateTypes(context);
        Type typeRight = expression.getExpressionType();
        
        // Validate left (propagate if needed)
        valueHolder.validateTypes(context);
        Type leftType = valueHolder.getExpressionType();
        
        // If left is NULL : only happens with a VAR declaration
        if(leftType.isNull()) {
            // is it an assignment
            if(valueHolder instanceof ReferenceExpression ref) {
                ref.signalType(leftType);
            }
            return;
        }
        
        // Left is not NULL (it's as such explicit)
        if(!Objects.equals(leftType, typeRight)) {
            throw new TypeException(expression, "Assignment for " + valueHolder + " expected " + leftType + ". Expression is of type " + typeRight);
        }
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleAffectVariable(this);
    }

    @Override
    public @NotNull String toString() {
        return valueHolder + " = " + expression;
    }

    /**
     * Parse a define statement.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
    public static @NotNull StatementNode parseNextDefine(@NotNull ExpressionNode affected, @NotNull TokenStream tokens) {
        ExpressionNode expression = ExpressionNode.readNextExpression(tokens);

        // optional ;
        tokens.dropOptional(TokenType.SEMI_COLON);

        return new AffectationStatement(affected, expression);
    }
}
