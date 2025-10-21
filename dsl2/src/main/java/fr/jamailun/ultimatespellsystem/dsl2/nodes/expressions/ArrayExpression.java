package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl2.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.*;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.ExpressionVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Raw array value.
 */
public class ArrayExpression extends ExpressionNode {

    @Getter
    private final List<ExpressionNode> elements;

    private Type selfType;
    private Type elementsType;

    protected ArrayExpression(TokenPosition position, List<ExpressionNode> elements) {
        super(position);
        this.elements = elements;
    }

    @Override
    public @NotNull Type getExpressionType() {
        if(selfType == null)
            throw new TypeException(firstTokenPosition(), "Type of primitive has not been set.");
        return selfType;
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleArray(this);
    }

    @Override
    public void validateTypes(@NotNull TypesContext contextParent) {
        // avoid non-intentional propagation
        TypesContext context = contextParent.childContext();

        // Check type for all expressions : they must share the same type.
        for(ExpressionNode node : elements) {
            node.validateTypes(context);

            if(elementsType == null) {
                elementsType = node.getExpressionType();
            } else {
                //TODO
                assertExpressionType(node, elementsType);
            }
        }

        // Array is empty : type is null as array ?
        if(elementsType == null) {
            elementsType = TypePrimitive.NULL.asType();
        }

        // Set self type at the end
        selfType = elementsType.pushArray();
    }

    @PreviousIndicator(expected = TokenType.SQUARE_BRACKET_OPEN)
    public static ArrayExpression parseRawArray(@NotNull TokenStream tokens) {
        TokenPosition pos = tokens.position();
        List<ExpressionNode> nodes = new ArrayList<>();
        boolean first = true;
        while(tokens.hasMore()) {
            Token peek = tokens.peek();
            if(peek.getType() == TokenType.SQUARE_BRACKET_CLOSE)
                break;
            if( ! first) {
                tokens.dropOrThrow(TokenType.COMMA);
            }
            first = false;
            ExpressionNode node = ExpressionNode.readNextExpression(tokens);
            nodes.add(node);
        }
        tokens.dropOrThrow(TokenType.SQUARE_BRACKET_CLOSE);
        return new ArrayExpression(pos, nodes);
    }

    @Override
    public String toString() {
        return "ARRAY("+(selfType==null?"? type":selfType)+")[" + elements + "]";
    }

}
