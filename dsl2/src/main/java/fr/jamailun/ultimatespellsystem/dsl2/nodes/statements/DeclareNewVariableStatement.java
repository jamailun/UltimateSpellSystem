package fr.jamailun.ultimatespellsystem.dsl2.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.StatementVisitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Define statement will set a variable to something.
 */
@Getter
@RequiredArgsConstructor
public class DeclareNewVariableStatement extends StatementNode {

    private final @NotNull String varType;
    private final @NotNull String varName;
    private final @Nullable ExpressionNode expression;

    public DeclareNewVariableStatement(@NotNull Token varType, @NotNull Token varName, @Nullable ExpressionNode expression) {
        this.varType = varType.getContentString();
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
        return varType + " " + varName + " = " + expression;
    }

    /**
     * Parse a define statement.
     * @param tokens streams of tokens.
     * @return a new instance.
     */
    public static @NotNull StatementNode parseNextDefine(@NotNull Token typeIdentifier, @NotNull Token varIdentifier, boolean hasValue, @NotNull TokenStream tokens) {
        String varType = typeIdentifier.getContentString();
        String varName = varIdentifier.getContentString();
        ExpressionNode expression = hasValue ? ExpressionNode.readNextExpression(tokens) : null;

        // optional ;
        tokens.dropOptional(TokenType.SEMI_COLON);

        return new DeclareNewVariableStatement(varType, varName, expression);
    }
}
