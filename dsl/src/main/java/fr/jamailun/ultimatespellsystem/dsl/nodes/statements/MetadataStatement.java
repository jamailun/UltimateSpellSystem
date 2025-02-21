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

@RequiredArgsConstructor
@Getter
public class MetadataStatement extends StatementNode {

    private final @NotNull String name;
    private final @NotNull List<ExpressionNode> params;

    @PreviousIndicator(expected = TokenType.CHAR_AT)
    public static @NotNull MetadataStatement parseMetadata(@NotNull TokenStream tokens) {
        List<ExpressionNode> nodes = new ArrayList<>();
        String name = tokens.nextOrThrow(TokenType.IDENTIFIER).getContentString();
        if(tokens.dropOptional(TokenType.BRACKET_OPEN)) {
            do {
                ExpressionNode child = ExpressionNode.readNextExpression(tokens, true);
                nodes.add(child);
            } while(tokens.dropOptional(TokenType.COMMA));
            tokens.dropOrThrow(TokenType.BRACKET_CLOSE);
        }
        return new MetadataStatement(name, nodes);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleMetadata(this);
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        params.forEach(n -> n.validateTypes(context.childContext()));
    }
}
