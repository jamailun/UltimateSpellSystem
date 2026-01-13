package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl2.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.*;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.ExpressionVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Raw properties set.
 */
@Getter
public class MapLiteral extends LiteralExpression<Map<String,ExpressionNode>> {

    private final Map<String, ExpressionNode> expressions;

    public MapLiteral(TokenPosition pos, Map<String, ExpressionNode> expressions) {
        super(pos);
        this.expressions = expressions;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return TypePrimitive.MAP.asType();
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleMapLiteral(this);
    }

    @Override
    public String toString() {
        return "MAP" + PREFIX + expressions + SUFFIX;
    }

    @Override
    public void validateTypes(@NotNull TypesContext contextParent) {
        TypesContext context = contextParent.childContext();
        for(ExpressionNode expression : expressions.values())
            expression.validateTypes(context);
    }

    @Override
    public Map<String, ExpressionNode> getRaw() {
        return expressions;
    }

    @PreviousIndicator(expected = {TokenType.BRACKET_OPEN})
    public static MapLiteral parseMap(TokenStream tokens) {
        TokenPosition pos = tokens.position();
        Map<String, ExpressionNode> expressions = new HashMap<>();
        while(tokens.hasMore()) {
            if(tokens.dropOptional(TokenType.BRACES_CLOSE)) {
                // EOP
                break;
            }
            if( ! expressions.isEmpty()) {
                tokens.dropOrThrow(TokenType.COMMA);
            }
            // KEY
            String propKey;
            Token word = tokens.next();
            if(word.getType() == TokenType.IDENTIFIER) {
                propKey = word.getContentString();
            } else if(word.getType().letters) {
                propKey = word.getType().name().toLowerCase();
            } else {
                throw new SyntaxException(word, "Unexpected token for property-key.");
            }
            // :
            tokens.dropOrThrow(TokenType.COLON);
            // VALUE
            ExpressionNode value = ExpressionNode.readNextExpression(tokens);
            // build
            expressions.put(propKey, value);
        }
        return new MapLiteral(pos, expressions);
    }
}
