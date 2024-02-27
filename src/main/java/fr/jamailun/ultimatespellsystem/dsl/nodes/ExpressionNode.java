package fr.jamailun.ultimatespellsystem.dsl.nodes;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.BiOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.NotOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.PotionEffect;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.bukkit.entity.EntityType;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

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
        return readNextExpression(tokens, allowCustom, new ExpressionParsingQueue(false));
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

    private final static List<TokenType> LOW_PRIORITY_OPERATORS = List.of(TokenType.OPE_ADD, TokenType.OPE_SUB);
    private final static List<TokenType> HIGH_PRIORITY_OPERATORS = List.of(TokenType.OPE_MUL, TokenType.OPE_DIV);

    private static ExpressionNode tryConvertOperations(ExpressionNode expr, TokenStream tokens, ExpressionParsingQueue stack) {
        //System.out.println("| TRY convert " + expr + ", stack = " + stack);
        Token token = tokens.peek();

        // Low-priority : push to stack
        if(LOW_PRIORITY_OPERATORS.contains(token.getType()) && !stack.priority) {
            tokens.drop();
            // push to stack
            stack.expressionStack.push(expr);
            stack.operandsStack.push(token);

            //System.out.println("| Pushing EXPR=" + expr);
            //System.out.println("| Pushing OPERAND=" + token);
            //System.out.println("| -> new stack = " + stack);

            // fetch next one
            return readNextExpression(tokens, true, stack);
        }

        // High-priority operators
        if(HIGH_PRIORITY_OPERATORS.contains(token.getType())) {
            tokens.drop();
            // don't push to stack. Get the next one and convert directly
            //System.out.println("| Priority operator ! (" + token + ")");
            ExpressionNode nextOne = readNextExpression(tokens, true, new ExpressionParsingQueue(true));
            //System.out.println("| NEXT = " + nextOne);
            BiOperator current = BiOperator.parseBiOperator(expr, token, nextOne);
            //System.out.println("| Built directly: " + current);
            // And redo (try to have more operators)
            return tryConvertOperations(current, tokens, stack);
        }

        // No operator after (EOE) : unstack the stack, build expression tree, return it.
        // System.out.println("| Will start popping all stack: " + expr);
        ExpressionNode topRight = expr;
        // Dépiler peu à peu
        while( ! stack.expressionStack.isEmpty()) {
            ExpressionNode node = stack.expressionStack.pop();
            Token ope = stack.operandsStack.pop();
        //    System.out.println("| Popping NODE=" + node);
        //    System.out.println("| Popping OPE =" + ope);
        //    System.out.println("| -> new stack = " + stack);
            // Fusion right operators
            topRight = BiOperator.parseBiOperator(node, ope, topRight);
        //    System.out.println("| -> Made " + topRight);
        }
        return topRight;
    }

    private static class ExpressionParsingQueue {
        final Deque<ExpressionNode> expressionStack = new ArrayDeque<>();
        final Deque<Token> operandsStack = new ArrayDeque<>();
        final boolean priority;

        ExpressionParsingQueue(boolean priority) {
            this.priority = priority;
        }

        @Override
        public String toString() {
            return "{"+(priority?"! ":"")+"E="+expressionStack+", O="+operandsStack+"}";
        }
    }

}
