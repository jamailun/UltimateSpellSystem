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
        return readNextExpression(tokens, allowCustom, new MathParsingQueue(false), LogicalParsingQueue.start());
    }

    private static ExpressionNode readNextExpression(TokenStream tokens, boolean allowCustom, MathParsingQueue mathStack, LogicalParsingQueue logicStack) {
        ExpressionNode raw = readNextExpressionBuffer(tokens, allowCustom);
        ExpressionNode withMath = tryConvertMathOperations(raw, tokens, mathStack);
        ExpressionNode withLogic = tryConvertLogicalExpression(withMath, tokens, logicStack);
        System.out.println("[PRODUCED] " + withLogic + " -- " + tokens);
        return withLogic;
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
    private final static List<TokenType> COMPARISON_OPERATORS = List.of(
            TokenType.COMP_NE, TokenType.COMP_EQ, TokenType.COMP_GT,
            TokenType.COMP_LT, TokenType.COMP_LE, TokenType.COMP_GE
    );

    private static ExpressionNode tryConvertMathOperations(ExpressionNode expr, TokenStream tokens, MathParsingQueue stack) {
        //System.out.println("M| TRY convert " + expr + ", stack = " + stack);
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
            return readNextExpression(tokens, true, stack, LogicalParsingQueue.start());
        }

        // High-priority operators
        if(HIGH_PRIORITY_OPERATORS.contains(token.getType())) {
            tokens.drop();
            // don't push to stack. Get the next one and convert directly
            //System.out.println("| Priority operator ! (" + token + ")");
            ExpressionNode nextOne = readNextExpression(tokens, true, new MathParsingQueue(true), LogicalParsingQueue.start());
            //System.out.println("| NEXT = " + nextOne);
            BiOperator current = BiOperator.parseBiOperator(expr, token, nextOne);
            //System.out.println("| Built directly: " + current);
            // And redo (try to have more operators)
            return tryConvertMathOperations(current, tokens, stack);
        }

        // No operator after (EOE) : unstack the stack, build expression tree, return it.
        // System.out.println("| Will start popping all stack: " + expr);
        return stack.deStack(expr);
    }

    private static ExpressionNode tryConvertLogicalExpression(ExpressionNode expr, TokenStream tokens, LogicalParsingQueue stack) {
        Token token = tokens.peek();
        System.out.println("L| Trying to convert " + expr + ". First ? " + stack.first);

        // Find a comparator ('==' for example).
        if(COMPARISON_OPERATORS.contains(token.getType())) {
            Token comparator = tokens.next();
            System.out.println("L| Got comparator : " + comparator);
            ExpressionNode left = stack.deStack(expr);
            System.out.println("L| => LEFT: " + left);
            ExpressionNode right = ExpressionNode.readNextExpression(tokens, true, new MathParsingQueue(false), LogicalParsingQueue.start());
            System.out.println("L| => RIGHT: " + right);
            return BiOperator.parseBiOperator(left, comparator, right);
        }

        // OR (+ non priority)
        if(token.getType() == TokenType.OPE_OR && !stack.priority) {
            // if
            tokens.drop();
            // push to stack
            stack.expressionStack.push(expr);
            stack.operandsStack.push(token);
            System.out.println("L| => OR ! " + stack);
            // fetch next one
            return readNextExpression(tokens, true, new MathParsingQueue(false), stack);
        }

        // AND
        if(token.getType() == TokenType.OPE_AND) {
            tokens.drop();
            // don't push to stack. Get the next one and convert directly
            ExpressionNode nextOne = readNextExpression(tokens, true, new MathParsingQueue(false), LogicalParsingQueue.priority());
            BiOperator current = BiOperator.parseBiOperator(expr, token, nextOne);
            // And redo (try to have more operators)
            return tryConvertLogicalExpression(current, tokens, stack);
        }

        // EOE !
        if(stack.first) {
            return stack.deStack(expr);
        }
        return new ConditionWrapperNodeExpression( stack.deStack(expr) );
    }

    private static class MathParsingQueue extends ParsingQueue {
        MathParsingQueue(boolean priority) {
            super(priority);
        }
    }

    private static class LogicalParsingQueue extends ParsingQueue {
        private final boolean first;
        LogicalParsingQueue(boolean priority, boolean first) {
            super(priority);
            this.first = first;
            assert !first || !priority;
        }
        static LogicalParsingQueue start() {
            return new LogicalParsingQueue(false, true);
        }
        static LogicalParsingQueue priority() {
            return new LogicalParsingQueue(true,  false);
        }
        static LogicalParsingQueue nonPriority() {
            return new LogicalParsingQueue(false,  false);
        }
    }

    private static abstract class ParsingQueue {
        final Deque<ExpressionNode> expressionStack = new ArrayDeque<>();
        final Deque<Token> operandsStack = new ArrayDeque<>();
        final boolean priority;

        ParsingQueue(boolean priority) {
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
                //    System.out.println("| Popping NODE=" + node);
                //    System.out.println("| Popping OPE =" + ope);
                //    System.out.println("| -> new stack = " + stack);
                // Fusion right operators
                topRight = BiOperator.parseBiOperator(node, ope, topRight);
                //    System.out.println("| -> Made " + topRight);
            }
            return topRight;
        }
    }

}
