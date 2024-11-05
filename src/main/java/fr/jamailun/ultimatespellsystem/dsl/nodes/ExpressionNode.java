package fr.jamailun.ultimatespellsystem.dsl.nodes;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.AllEntitiesAroundExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.PositionOfExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.SizeOfExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.BiOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.NotOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.operators.SubOperator;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.PotionEffect;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.registries.CustomExpression;
import fr.jamailun.ultimatespellsystem.dsl.registries.EntityTypeRegistry;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * An "expression" is an element of the AST that can have, by itself, a type.
 */
public abstract class ExpressionNode extends Node {

    private final TokenPosition position;

    protected ExpressionNode(@NotNull TokenPosition position) {
        this.position = position;
    }

    public @NotNull TokenPosition firstTokenPosition() {
        return position;
    }

    public abstract @NotNull Type getExpressionType();

    public abstract void visit(@NotNull ExpressionVisitor visitor);

    public static ExpressionNode readNextExpression(TokenStream tokens) {
        return readNextExpression(tokens, false);
    }

    public static ExpressionNode readNextExpression(TokenStream tokens, boolean allowCustom) {
        return readNextExpression(tokens, allowCustom, new MathParsingQueue(false), true);
    }

    private static ExpressionNode readNextExpression(TokenStream tokens, boolean allowCustom, MathParsingQueue mathStack, boolean logicFirst) {
        ExpressionNode raw = readNextExpressionBuffer(tokens, allowCustom);
        // Check if the element is accessed !
        if(tokens.dropOptional(TokenType.SQUARE_BRACKET_OPEN)) {
            ExpressionNode index = readNextExpression(tokens);
            tokens.dropOrThrow(TokenType.SQUARE_BRACKET_CLOSE);
            raw = new ArrayGetterExpression(raw, index);
        }

        ExpressionNode withMath = tryConvertMathOperations(raw, tokens, mathStack);
        return tryConvertLogicalExpression(withMath, tokens, logicFirst);
    }

    private static @NotNull ExpressionNode readNextExpressionBuffer(@NotNull TokenStream tokens, boolean allowCustom) {
        Token token = tokens.next();
        return switch (token.getType()) {
            // Mono-operators
            case OPE_NOT -> {
                ExpressionNode nextExpression = readNextExpression(tokens);
                yield new NotOperator(token, nextExpression);
            }
            case OPE_SUB -> {
                TokenPosition position = tokens.position();
                ExpressionNode nextExpression = readNextExpression(tokens);
                yield new SubOperator(position, new NumberExpression(position, 0), nextExpression);
            }
            // Literals
            case VALUE_NUMBER -> new NumberExpression(token);
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
                if(EntityTypeRegistry.isAllowed(value)) {
                    yield new EntityTypeExpression(token.pos(), value);
                } else if("EntityType".equals(value)) {
                    tokens.dropOrThrow(TokenType.DOT);
                    Token real = tokens.nextOrThrow(TokenType.IDENTIFIER);
                    String v = real.getContentString();
                    if(EntityTypeRegistry.isAllowed(v)) {
                        yield new EntityTypeExpression(token.pos(), v);
                    } else {
                        throw new SyntaxException(token, "Unknown EntityType '" + v + "'.");
                    }
                }

                // Custom value : checked at runtime !
                if(allowCustom) {
                    yield new RuntimeLiteral(token);
                }

                throw new SyntaxException(token, "Expected an expression.");
            }
            case CHAR_AT -> LocationLiteral.readNextLocation(tokens);
            // Var
            case VALUE_VARIABLE -> new VariableExpression(token);
            // Openers: '{{', '[[', '('
            case PROPERTY_OPEN -> PropertiesExpression.parseProperties(tokens);
            case ARRAY_OPEN -> ArrayExpression.parseNextArrayConcat(tokens);
            case BRACKET_OPEN -> ParenthesisExpression.parseParenthesis(tokens);

            // 'function'-expressions
            case ALL -> AllEntitiesAroundExpression.parseAllExpression(tokens);
            case POSITION -> PositionOfExpression.parsePositionOf(tokens);
            case CALL -> CustomExpression.parseCustomExpressionCall(tokens);
            case SIZEOF -> SizeOfExpression.parseSizeOf(tokens);

            // Other
            default -> throw new SyntaxException(token, "Unexpected expression-start.");
        };
    }

    private final static List<TokenType> LOW_PRIORITY_OPERATORS = List.of(TokenType.OPE_ADD, TokenType.OPE_SUB);
    private final static List<TokenType> HIGH_PRIORITY_OPERATORS = List.of(TokenType.OPE_MUL, TokenType.OPE_DIV);

    private static ExpressionNode tryConvertMathOperations(ExpressionNode expr, TokenStream tokens, MathParsingQueue stack) {
        Token token = tokens.peek();

        // Low-priority : push to stack
        if(LOW_PRIORITY_OPERATORS.contains(token.getType()) && !stack.priority) {
            tokens.drop();
            // push to stack
            stack.expressionStack.push(expr);
            stack.operandsStack.push(token);
            // fetch next one
            return readNextExpression(tokens, true, stack, true);
        }

        // High-priority operators
        if(HIGH_PRIORITY_OPERATORS.contains(token.getType())) {
            tokens.drop();
            // don't push to stack. Get the next one and convert directly
            ExpressionNode nextOne = readNextExpression(tokens, true, new MathParsingQueue(true), true);
            BiOperator current = BiOperator.parseBiOperator(expr, token, nextOne);
            // And redo (try to have more operators)
            return tryConvertMathOperations(current, tokens, stack);
        }

        // No operator after (EOE) : unstack the stack, build expression tree, return it.
        return stack.deStack(expr);
    }

    private static ExpressionNode tryConvertLogicalExpression(ExpressionNode expr, TokenStream tokens, boolean firstLevel) {
        Token token = tokens.peek();
        return switch(token.getType()) {
            case OPE_OR, OPE_AND -> {
                if(!firstLevel)
                    yield expr;
                tokens.drop();
                ExpressionNode right = readNextExpression(tokens, false, new MathParsingQueue(false), true);
                yield BiOperator.parseBiOperator(expr, token, right);
            }
            case COMP_NE, COMP_EQ, COMP_GT, COMP_LT, COMP_LE, COMP_GE -> {
                tokens.drop();
                ExpressionNode right = readNextExpression(tokens, false, new MathParsingQueue(false), false);
                BiOperator newFormed = BiOperator.parseBiOperator(expr, token, right);
                yield tryConvertLogicalExpression(newFormed, tokens, true);
            }
            default -> expr;
        };
    }

    private static class MathParsingQueue {
        final Deque<ExpressionNode> expressionStack = new ArrayDeque<>();
        final Deque<Token> operandsStack = new ArrayDeque<>();
        final boolean priority;

        MathParsingQueue(boolean priority) {
            this.priority = priority;
        }

        @Override
        public String toString() {
            return "{"+(priority?"! ":"")+"E="+expressionStack+", O="+operandsStack+"}";
        }

        ExpressionNode deStack(ExpressionNode expr) {
            ExpressionNode topRight = expr;
            // Dépiler peu à peu
            while( ! expressionStack.isEmpty()) {
                ExpressionNode node = expressionStack.pop();
                Token ope = operandsStack.pop();
                // Fusion right operators
                topRight = BiOperator.parseBiOperator(node, ope, topRight);
            }
            return topRight;
        }
    }

}
