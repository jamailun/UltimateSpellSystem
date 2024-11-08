package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallStatement extends StatementNode {

    private final String functionId;
    private final List<ExpressionNode> arguments;

    public FunctionCallStatement(String functionId, List<ExpressionNode> arguments) {
        this.functionId = functionId;
        this.arguments = arguments;
    }

    @Override
    public void validateTypes(TypesContext contextRaw) {
        TypesContext context = contextRaw.childContext();
        arguments.forEach(e -> e.validateTypes(context));
    }

    @Override
    public void visit(StatementVisitor visitor) {
        visitor.functionCall(this);
    }

    @PreviousIndicator(expected = TokenType.CALL)
    public static FunctionCallStatement parseFunctionCall(TokenStream tokens) {
        tokens.assertNextIs(TokenType.VALUE_STRING, TokenType.IDENTIFIER);
        String functionId = tokens.next().getContentString();
        List<ExpressionNode> args = new ArrayList<>();
        if(tokens.dropOptional(TokenType.BRACKET_OPEN)) {
            do {
                ExpressionNode arg = ExpressionNode.readNextExpression(tokens);
                if( ! args.isEmpty())
                    tokens.dropOrThrow(TokenType.COMMA);
                args.add(arg);
            } while( ! tokens.dropOptional(TokenType.BRACKET_CLOSE));
        }
        return new FunctionCallStatement(functionId, args);
    }

    public String getFunctionId() {
        return functionId;
    }

    public List<ExpressionNode> getArguments() {
        return arguments;
    }
}
