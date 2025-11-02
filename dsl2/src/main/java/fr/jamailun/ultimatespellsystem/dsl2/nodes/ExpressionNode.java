package fr.jamailun.ultimatespellsystem.dsl2.nodes;

import fr.jamailun.ultimatespellsystem.dsl2.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl2.errors.UssException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators.BiOperator;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators.IncrementExpression;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators.NotOperator;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators.SubOperator;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.*;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * An "expression" is a typed element, used by {@link StatementNode statements}. <br/>
 * Types are statically checked.<br/>
 * This class can parse expressions using the {@link #readNextExpression(TokenStream)} method.
 */
public abstract class ExpressionNode extends Node {

    private final TokenPosition position;

    /**
     * New instance of an expression, needs a position.
     * @param position non-null position of the first token.
     */
    protected ExpressionNode(@NotNull TokenPosition position) {
        this.position = position;
    }

    /**
     * Get the position of the first token of the expression.
     * @return a non-null token position.
     */
    public @NotNull TokenPosition firstTokenPosition() {
        return position;
    }

    /**
     * Get the {@link Type} of this expression.
     * @return a valid and non-null Type.
     */
    public abstract @NotNull Type getExpressionType();

    /**
     * Make a Visitor visit this node.
     * @param visitor the non-null visitor to call.
     */
    public abstract void visit(@NotNull ExpressionVisitor visitor);

    /**
     * Extract the next Expression-Node from a tokens-stream.
     * @param tokens the tokens-stream to create the next expression with.
     * @return a non-null expression.
     * @throws UssException if a problems occurs.
     */
    public static @NotNull ExpressionNode readNextExpression(@NotNull TokenStream tokens) {
        return readNextExpression(tokens, new MathParsingQueue(false), true);
    }

    private static @NotNull ExpressionNode readNextExpression(TokenStream tokens, MathParsingQueue mathStack, boolean logicFirst) {
        ExpressionNode raw = readNextExpressionBuffer(tokens);
        // Check if the element is accessed !
        if(tokens.dropOptional(TokenType.SQUARE_BRACKET_OPEN)) {
            ExpressionNode index = readNextExpression(tokens);
            tokens.dropOrThrow(TokenType.SQUARE_BRACKET_CLOSE, "A ')' is required after an expression that started with a '('.");
            raw = new ArrayGetterExpression(raw, index);
        }

        ExpressionNode withMath = tryConvertMathOperations(raw, tokens, mathStack);
        return tryConvertLogicalExpression(withMath, tokens, logicFirst);
    }

    private static @NotNull ExpressionNode readNextExpressionBuffer(@NotNull TokenStream tokens) {
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
                yield new SubOperator(position, new NumberLiteral(position, 0), nextExpression);
            }

            // Literals
            case VALUE_NUMBER -> new NumberLiteral(token);
            case VALUE_BOOLEAN -> new BooleanLiteral(token);
            case VALUE_STRING -> new StringLiteral(token);
            case VALUE_DURATION -> new DurationLiteral(token);
            case NULL -> new NullLiteral(token.pos());
            case CHAR_AT -> LocationLiteral.readNextLocation(tokens);

            // Increment / decrement
            case INCREMENT -> IncrementExpression.parseIncrementOrDecrement(tokens, true);
            case DECREMENT -> IncrementExpression.parseIncrementOrDecrement(tokens, false);

            // Toutes les compositions de
            // "A.B", "A.B(...)", "A[B]" et "A".
            case IDENTIFIER -> parseIdentifierExpression(token, tokens);


            // Openers: '{', '[', '('
            case BRACES_OPEN -> MapLiteral.parseMap(tokens);
            case SQUARE_BRACKET_OPEN -> ArrayLiteral.readNextArrayLiteral(tokens);
            case BRACKET_OPEN -> ParenthesisExpression.parseParenthesis(tokens);


            // Other
            default -> throw new SyntaxException(token, "Unexpected expression-start.");
        };
    }

    @PreviousIndicator(expected = TokenType.IDENTIFIER)
    public static @NotNull ExpressionNode parseIdentifierExpression(@NotNull Token first, @NotNull TokenStream tokens) {
        if(tokens.dropOptional(TokenType.INCREMENT)) {
            return new IncrementExpression(first, true, true);
        } else if(tokens.dropOptional(TokenType.DECREMENT)) {
            return new IncrementExpression(first, false, true);
        }

        // IDENTIFIER( ?
        if(tokens.dropOptional(TokenType.BRACKET_OPEN)) {
            List<ExpressionNode> arguments = parseArgumentsParameter(tokens);
            ExpressionNode expression = new FunctionCallExpression(null, first, arguments);
            return parseIdentifierExpression(expression, tokens);
        }


        ExpressionNode left = new ReferenceExpression(first);
        return parseIdentifierExpression(left, tokens);
    }

    // a.b.c
    // first = 'a'
    private static @NotNull ExpressionNode parseIdentifierExpression(@NotNull ExpressionNode left, @NotNull TokenStream tokens) {
        // IDENTIFIER. ?
        if(tokens.dropOptional(TokenType.DOT)) {
            // Il faut une expression !
            // i.e. "a.IDENTIFIER" !
            Token identifier = tokens.nextOrThrow(TokenType.IDENTIFIER, "After 'IDENTIFIER.' can only accept a 'IDENTIFIER'.");

            // Parenthèse ?
            // a.IDENTIFIER(...)
            if(tokens.dropOptional(TokenType.BRACKET_OPEN)) {
                List<ExpressionNode> arguments = parseArgumentsParameter(tokens);
                ExpressionNode fctCall = new FunctionCallExpression(left, identifier, arguments);

                // Après "a.b(...)" on peut avoir une suite. Donc, on refait une boucle.
                return parseIdentifierExpression(fctCall, tokens);
            }

            // Pas de parenthèse, on a bien un simple "a.b"
            // On package le tout en une expression
            ExpressionNode fieldGet = new FieldGetExpression(left, identifier.getContentString());

            // (a.b).c
            return parseIdentifierExpression(fieldGet, tokens);
        }

        // IDENTIFIER[ ?
        if(tokens.dropOptional(TokenType.SQUARE_BRACKET_OPEN)) {
            ExpressionNode index = ExpressionNode.readNextExpression(tokens);
            tokens.dropOrThrow(TokenType.SQUARE_BRACKET_CLOSE, "Expected an array-get close ']' after it was opened.");
            ArrayGetterExpression arrayGet = new ArrayGetterExpression(left, index);

            // On peut avoir "a[b].c", donc on refait un tour
            return parseIdentifierExpression(arrayGet, tokens);
        }

        // à priori, cas par défaut : on renvoie tout simplement l'expression qu'on a reçu.
        // Dans le cas VRAIMENT par défaut, c'est donc une référence de variable.
        return left;
    }

    public static @NotNull List<ExpressionNode> parseArgumentsParameter(@NotNull TokenStream tokens) {
        List<ExpressionNode> list = new ArrayList<>();
        boolean first = true;
        while(!tokens.dropOptional(TokenType.BRACKET_CLOSE)) {
            if(first) first = false; else tokens.dropOrThrow(TokenType.COMMA, "A COMMA is required between parameters.");
            ExpressionNode expression = ExpressionNode.readNextExpression(tokens);
            list.add(expression);
        }
        return list;
    }

    private final static List<TokenType> LOW_PRIORITY_OPERATORS = List.of(TokenType.OPE_ADD, TokenType.OPE_SUB);
    private final static List<TokenType> HIGH_PRIORITY_OPERATORS = List.of(TokenType.OPE_MUL, TokenType.OPE_DIV);

    private static ExpressionNode tryConvertMathOperations(ExpressionNode expr, @NotNull TokenStream tokens, MathParsingQueue stack) {
        Token token = tokens.peek();

        // Low-priority : push to stack
        if(LOW_PRIORITY_OPERATORS.contains(token.getType()) && !stack.priority) {
            tokens.drop();
            // push to stack
            stack.expressionStack.push(expr);
            stack.operandsStack.push(token);
            // fetch next one
            return readNextExpression(tokens, stack, true);
        }

        // High-priority operators
        if(HIGH_PRIORITY_OPERATORS.contains(token.getType())) {
            tokens.drop();
            // don't push to stack. Get the next one and convert directly
            ExpressionNode nextOne = readNextExpression(tokens, new MathParsingQueue(true), true);
            BiOperator current = BiOperator.parseBiOperator(expr, token, nextOne);
            // And redo (try to have more operators)
            return tryConvertMathOperations(current, tokens, stack);
        }

        // No operator after (EOE) : unstack the stack, build expression tree, return it.
        return stack.deStack(expr);
    }

    private static @NotNull ExpressionNode tryConvertLogicalExpression(ExpressionNode expr, @NotNull TokenStream tokens, boolean firstLevel) {
        Token token = tokens.peek();
        return switch(token.getType()) {
            case OPE_OR, OPE_AND  -> {
                if(!firstLevel)
                    yield expr;
                tokens.drop();
                ExpressionNode right = readNextExpression(tokens, new MathParsingQueue(false), true);
                yield BiOperator.parseBiOperator(expr, token, right);
            }
            case COMP_NE, COMP_EQ, COMP_GT, COMP_LT, COMP_LE, COMP_GE -> {
                tokens.drop();
                ExpressionNode right = readNextExpression(tokens, new MathParsingQueue(false), false);
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
