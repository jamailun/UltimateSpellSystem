package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;

import java.util.HashMap;
import java.util.Map;

public class PropertiesExpression extends ExpressionNode {

    private final Map<String, ExpressionNode> expressions;

    public PropertiesExpression(TokenPosition pos, Map<String, ExpressionNode> expressions) {
        super(pos);
        this.expressions = expressions;
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.PROPERTIES_SET.asType();
    }

    @Override
    public void visit(ExpressionVisitor visitor) {
        visitor.handlePropertiesSet(this);
    }

    @Override
    public String toString() {
        return "{" + expressions + "}";
    }

    @Override
    public void validateTypes(TypesContext context) {
        // Nothing here
    }

    public Map<String, ExpressionNode> getExpressions() {
        return expressions;
    }

    @PreviousIndicator(expected = {TokenType.PROPERTY_OPEN})
    public static PropertiesExpression parseProperties(TokenStream tokens) {
        TokenPosition pos = tokens.position();
        Map<String, ExpressionNode> expressions = new HashMap<>();
        while(tokens.hasMore()) {
            if(tokens.peek().getType() == TokenType.PROPERTY_CLOSE) {
                // EOP
                break;
            }
            if( ! expressions.isEmpty()) {
                tokens.dropOrThrow(TokenType.COMMA);
            }
            // KEY
            Token word = tokens.nextOrThrow(TokenType.IDENTIFIER);
            // :
            tokens.dropOrThrow(TokenType.COLON);
            // VALUE
            ExpressionNode value = ExpressionNode.readNextExpression(tokens, true);
            // build
            expressions.put(word.getContentString(), value);
        }
        tokens.dropOrThrow(TokenType.PROPERTY_CLOSE);
        return new PropertiesExpression(pos, expressions);
    }
}