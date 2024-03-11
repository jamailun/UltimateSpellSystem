package fr.jamailun.ultimatespellsystem.dsl.registries;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.FunctionCallStatement;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A custom expression, able to
 */
public abstract class CustomExpression extends ExpressionNode {

    /**
     * An immutable list of arguments. Never null.
     */
    protected final List<ExpressionNode> arguments;

    public CustomExpression(TokenPosition position, List<ExpressionNode> arguments) {
        super(position);
        this.arguments = List.copyOf(arguments);
    }

    public abstract String getLabel();

    protected abstract List<TypedArgument> getArguments();

    @Override
    public final void visit(ExpressionVisitor visitor) {
        visitor.handleCustomExpression(this);
    }

    @Override
    public final void validateTypes(TypesContext contextRaw) {
        TypesContext context = contextRaw.childContext();

        // Check argument amount
        List<TypedArgument> listArgs = getArguments();
        if(arguments.size() != listArgs.size())
            throw new SyntaxException(firstTokenPosition(), "Invalid arguments count. Expected " + listArgs.size() + ", got " + arguments.size());

        // Then validate arguments types
        for(int i = 0; i < listArgs.size(); i++) {
            TypedArgument theory = listArgs.get(i);
            ExpressionNode reality = arguments.get(i);
            reality.validateTypes(context);
            if(theory.primitive() != reality.getExpressionType().primitive()) {
                throw new TypeException(reality, "Invalid argument type '"+theory.argName()+"', expected " + theory.primitive() + " but got " + reality.getExpressionType());
            }
            if(!theory.allowFilter().isValid(reality.getExpressionType())) {
                throw new TypeException(reality, "Argument '" + theory.argName() + "' has filter " + theory.allowFilter() + ", but got " + reality.getExpressionType());
            }
        }
    }

    public record TypedArgument(String argName, TypePrimitive primitive, CollectionFilter allowFilter) {}


    @PreviousIndicator(expected = TokenType.CALL)
    public static CustomExpression parseCustomExpressionCall(TokenStream tokens) {
        tokens.assertNextIs(TokenType.VALUE_STRING, TokenType.IDENTIFIER);
        Token tokenLabel = tokens.next();
        String expressionId = tokenLabel.getContentString();
        CustomExpressionsRegistry.CustomExpressionProvider cep = CustomExpressionsRegistry.find(expressionId);
        if(cep == null)
            throw new SyntaxException(tokenLabel, "Unknown custom expression'" + expressionId + "'.");

        List<ExpressionNode> args = new ArrayList<>();
        if(tokens.dropOptional(TokenType.BRACKET_OPEN)) {
            do {
                ExpressionNode arg = ExpressionNode.readNextExpression(tokens);
                if( ! args.isEmpty())
                    tokens.dropOrThrow(TokenType.COMMA);
                args.add(arg);
            } while( ! tokens.dropOptional(TokenType.BRACKET_CLOSE));
        }
        return cep.apply(args);
    }

}
