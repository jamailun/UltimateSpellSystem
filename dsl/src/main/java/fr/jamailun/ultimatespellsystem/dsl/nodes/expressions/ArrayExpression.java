package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
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
    private TypePrimitive typePrimitive;

    protected ArrayExpression(TokenPosition position, List<ExpressionNode> elements) {
        super(position);
        this.elements = elements;
    }

    @Override
    public @NotNull Type getExpressionType() {
        if(typePrimitive == null)
            throw new TypeException(firstTokenPosition(), "Type of primitive has not been set.");
        return typePrimitive.asType(true);
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

            if(typePrimitive == null) {
                typePrimitive = node.getExpressionType().primitive();
            } else {
                assertExpressionType(node, CollectionFilter.MONO_ELEMENT, typePrimitive);
            }
        }
        if(typePrimitive == null)
            typePrimitive = TypePrimitive.NULL;
    }

    @PreviousIndicator(expected = TokenType.ARRAY_OPEN)
    public static ArrayExpression parseNextArrayConcat(TokenStream tokens) {
        TokenPosition pos = tokens.position();
        List<ExpressionNode> nodes = new ArrayList<>();
        boolean first = true;
        while(tokens.hasMore()) {
            Token peek = tokens.peek();
            if(peek.getType() == TokenType.ARRAY_CLOSE)
                break;
            if( ! first) {
                tokens.dropOrThrow(TokenType.COMMA);
            }
            first = false;
            ExpressionNode node = ExpressionNode.readNextExpression(tokens);
            nodes.add(node);
        }
        tokens.dropOrThrow(TokenType.ARRAY_CLOSE);
        return new ArrayExpression(pos, nodes);
    }

    @Override
    public String toString() {
        return "ARRAY("+(typePrimitive==null?"? type":typePrimitive.asType())+")[" + elements + "]";
    }

}
