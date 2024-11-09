package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class FunctionCallStatement extends StatementNode {

    private final String functionId;
    private final List<ExpressionNode> arguments;

    @Override
    public void validateTypes(@NotNull TypesContext contextRaw) {
        TypesContext context = contextRaw.childContext();
        arguments.forEach(e -> e.validateTypes(context));
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.functionCall(this);
    }

    @PreviousIndicator(expected = TokenType.CALL)
    public static @NotNull FunctionCallStatement parseFunctionCall(@NotNull TokenStream tokens) {
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

}
