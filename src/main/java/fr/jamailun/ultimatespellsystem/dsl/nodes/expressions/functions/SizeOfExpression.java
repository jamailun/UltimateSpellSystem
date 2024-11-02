package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import lombok.Getter;

@Getter
public class SizeOfExpression extends ExpressionNode {

    private final ExpressionNode child;

    protected SizeOfExpression(TokenPosition position, ExpressionNode child) {
        super(position);
        this.child = child;
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.NUMBER.asType();
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
        visitor.handleSizeOf(this);
    }

    @Override
    public void validateTypes(TypesContext context) {
        // Always valid :)
    }

    public static SizeOfExpression parseSizeOf(TokenStream tokens) {
        TokenPosition position = tokens.position();
        boolean parenthesis = tokens.dropOptional(TokenType.BRACKET_OPEN);

        ExpressionNode child = ExpressionNode.readNextExpression(tokens);

        if(parenthesis)
            tokens.dropOrThrow(TokenType.BRACKET_CLOSE);

        return new SizeOfExpression(position, child);
    }

    @Override
    public String toString() {
        return "size-of(" + child + ")";
    }
}
