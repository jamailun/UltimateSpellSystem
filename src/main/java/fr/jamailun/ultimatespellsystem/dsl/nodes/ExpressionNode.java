package fr.jamailun.ultimatespellsystem.dsl.nodes;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.AllEntitiesAround;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.ArrayConcatExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.VariableExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import org.bukkit.entity.EntityType;

public abstract class ExpressionNode extends Node {

    private final TokenPosition position;

    protected ExpressionNode(TokenPosition position) {
        this.position = position;
    }

    public TokenPosition firstTokenPosition() {
        return position;
    }

    public abstract Type getExpressionType();


    public static ExpressionNode readNextExpression(TokenStream tokens) {
        return readNextExpression(tokens, false);
    }

    public static ExpressionNode readNextExpression(TokenStream tokens, boolean allowCustom) {
        Token token = tokens.next();
        return switch (token.getType()) {
            // Literals
            case VALUE_NUMBER -> new NumberExpression(token); // UNIT !!!
            case TRUE, FALSE -> new BooleanExpression(token);
            case VALUE_STRING -> new StringExpression(token);
            case VALUE_DURATION -> new DurationExpression(token);
            case NULL -> new NullExpression(token.pos());
            case IDENTIFIER -> {
                String value = token.getContentString();
                // Potion effect ?
                EffectTypeExpression.PotionEffect effect = EffectTypeExpression.PotionEffect.find(value);
                if(effect != null)
                    yield new EffectTypeExpression(token.pos(), effect);

                // EntityType ?
                EntityType entityType = EntityTypeExpression.fromString(value);
                if(entityType != null)
                    yield new EntityTypeExpression(token.pos(), entityType);

                // Custom value : checked at runtime !
                if(allowCustom) {
                    yield new RuntimeLiteral(token);
                }

                throw new SyntaxException(token, "Expected an expression.");
            }
            // Var
            case VALUE_VARIABLE -> new VariableExpression(token);
            // Concat array
            case SQUARE_BRACKET_OPEN -> ArrayConcatExpression.parseNextArrayConcat(tokens);
            // all entities around
            case ALL -> AllEntitiesAround.parseAllExpression(tokens);

            // Other
            default -> throw new SyntaxException(token, "Unexpected expression-start.");
        };
    }

}
