package fr.jamailun.ultimatespellsystem.dsl.nodes;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.math.BiOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.math.NotOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.PotionEffect;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.bukkit.entity.EntityType;

import java.util.ArrayDeque;
import java.util.Deque;

public abstract class ExpressionNode extends Node {

    private final TokenPosition position;

    protected ExpressionNode(TokenPosition position) {
        this.position = position;
    }

    public TokenPosition firstTokenPosition() {
        return position;
    }

    public abstract Type getExpressionType();

    public abstract void visit(ExpressionVisitor visitor);

    public static ExpressionNode readNextExpression(TokenStream tokens) {
        return readNextExpression(tokens, false);
    }

    public static ExpressionNode readNextExpression(TokenStream tokens, boolean allowCustom) {
        return readNextExpression(tokens, allowCustom, new ExpressionParsingQueue());
    }

    private static ExpressionNode readNextExpression(TokenStream tokens, boolean allowCustom, ExpressionParsingQueue stack) {
        ExpressionNode raw = readNextExpressionBuffer(tokens, allowCustom);
        return tryConvertOperations(raw, tokens, stack);
    }

    private static ExpressionNode readNextExpressionBuffer(TokenStream tokens, boolean allowCustom) {
        Token token = tokens.next();
        return switch (token.getType()) {
            // Mono-operators
            case OPE_NOT -> {
                ExpressionNode nextExpression = readNextExpression(tokens);
                yield new NotOperator(token, nextExpression);
            }
            // Literals
            case VALUE_NUMBER -> new NumberExpression(token); // UNIT !!!
            case TRUE, FALSE -> new BooleanExpression(token);
            case VALUE_STRING -> new StringExpression(token);
            case VALUE_DURATION -> new DurationExpression(token);
            case NULL -> new NullExpression(token.pos());
            case IDENTIFIER -> {
                String value = token.getContentString();
                // Potion effect ?
                PotionEffect effect = PotionEffect.find(value);
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
            // Openers: '{{', '[', '('
            case PROPERTY_OPEN -> PropertiesExpression.parseProperties(tokens);
            case SQUARE_BRACKET_OPEN -> ArrayConcatExpression.parseNextArrayConcat(tokens);
            case BRACKET_OPEN -> ParenthesisExpression.parseParenthesis(tokens);
            // all entities around
            case ALL -> AllEntitiesAround.parseAllExpression(tokens);

            // Other
            default -> throw new SyntaxException(token, "Unexpected expression-start.");
        };
    }

    private static ExpressionNode tryConvertOperations(ExpressionNode expr, TokenStream tokens, ExpressionParsingQueue stack) {
        Token token = tokens.peek();
        return switch (token.getType()) {
            // Low-priority : push to stack
            case OPE_ADD, OPE_SUB -> {
                tokens.drop();
                // push to stack
                stack.expressionStack.push(expr);
                stack.operandsStack.push(token);
                // fetch next one
                yield readNextExpression(tokens, true, stack);
            }
            // High-priority operators
            case OPE_MUL, OPE_DIV -> {
                tokens.drop();
                // don't push to stack. Get the next one and convert directly
                ExpressionNode nextOne = readNextExpression(tokens, true, stack);
                BiOperator current = BiOperator.parseBiOperator(expr, token, nextOne);
                // And redo (try to have more operators)
                yield tryConvertOperations(current, tokens, stack);
            }
            // No operator after (EOE) : unstack the stack, build expression tree, return it.
            default -> {
                // Dépiler peu à peu
                ExpressionNode topRight = expr;
                while( ! stack.expressionStack.isEmpty()) {
                    ExpressionNode node = stack.expressionStack.pop();
                    Token ope = stack.operandsStack.pop();
                    topRight = BiOperator.parseBiOperator(node, ope, topRight);
                }
                yield topRight;
            }
        };
    }

    private static class ExpressionParsingQueue {
        final Deque<ExpressionNode> expressionStack = new ArrayDeque<>();
        final Deque<Token> operandsStack = new ArrayDeque<>();
    }

}
