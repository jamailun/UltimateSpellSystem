package fr.jamailun.ultimatespellsystem.dsl2.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl2.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl2.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.StatementVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Define statement will set a variable to something.
 */
@Getter
public class DeclareNewVariableStatement extends StatementNode {

    private final @NotNull TokenPosition position;
    private final @Nullable String varType;
    private final @NotNull String varName;
    private final @Nullable ExpressionNode expression;

    public DeclareNewVariableStatement(@Nullable Token varType, @NotNull Token varName, @Nullable ExpressionNode expression) {
        this.position = varName.pos();
        this.varType = varType == null ? null : varType.getContentString();
        this.varName = varName.getContentString();
        this.expression = expression;
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        // 1. Check the variable does not already exist ?
        var existingVariable = context.findVariable(varName);
        if(existingVariable != null) {
            throw new SyntaxException(position, "Variable '" + varName + "' is already set.");
        }

        // 2. At least the type or the expression must be defined
        if(varType == null && expression == null) {
            throw new SyntaxException(position, "Variable '" + varName + "' cannot use the 'var' keyword without direct assignation.");
        }

        // 3. Validate expression if possible.
        Type type;
        if(expression != null) {
            expression.validateTypes(context);
            type = expression.getExpressionType();
        } else {
            type = Type.ofAny(varType);
        }

        // 4. If the type is explicit, check it matches
        if(varType != null) {
            if(!Objects.equals(Type.ofAny(varType), type)) {
                throw new TypeException(position, "Assignment for " + varName + " expected " + varType + ". Expression is of type " + type);
            }
        }

        // 5. Register the variable
        context.registerVariable(position, varName, type);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleDeclareVariable(this);
    }

    @Override
    public String toString() {
        return Objects.requireNonNullElse(varType, "VAR") + " " + varName + (expression == null ? "" : " = " + expression);
    }
}
