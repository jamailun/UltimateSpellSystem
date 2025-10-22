package fr.jamailun.ultimatespellsystem.dsl2.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.StatementVisitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Define statement will set a variable to something.
 */
@Getter
@RequiredArgsConstructor
public class DeclareNewVariableStatement extends StatementNode {

    private final @Nullable String varType;
    private final @NotNull String varName;
    private final @Nullable ExpressionNode expression;

    public DeclareNewVariableStatement(@Nullable Token varType, @NotNull Token varName, @Nullable ExpressionNode expression) {
        this.varType = varType == null ? null : varType.getContentString();
        this.varName = varName.getContentString();
        this.expression = expression;
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        if(expression != null) {
            expression.validateTypes(context);
            //TODO
            context.registerVariable(varName, expression);
        }
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
