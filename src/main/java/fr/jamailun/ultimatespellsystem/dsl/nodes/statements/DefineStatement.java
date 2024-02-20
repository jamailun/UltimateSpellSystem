package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.Visitor;

public class DefineStatement extends StatementNode {

    private final String varName;
    private final ExpressionNode expression;

    public DefineStatement(String varName, ExpressionNode expression) {
        this.varName = varName;
        this.expression = expression;
    }

    public String getVarName() {
        return varName;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public void validateTypes(TypesContext context) {
        expression.validateTypes(context);
        // Register variable
        context.registerVariable(varName, expression);
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.handleDefine(this);
    }

    @Override
    public String toString() {
        return "DEFINE{%" + varName + " <- " + expression + "}";
    }

    @PreviousIndicator(expected = {TokenType.DEFINE})
    public static DefineStatement parseNextDefine(TokenStream tokens) {
        // %VAR_NAME
        Token varToken = tokens.nextOrThrow(TokenType.VALUE_VARIABLE);
        String varName = varToken.getContentString();

        // =
        tokens.dropOrThrow(TokenType.EQUAL);

        ExpressionNode expression = ExpressionNode.readNextExpression(tokens);

        // optional ;
        tokens.dropOptional(TokenType.SEMI_COLON);

        return new DefineStatement(varName, expression);
    }
}
