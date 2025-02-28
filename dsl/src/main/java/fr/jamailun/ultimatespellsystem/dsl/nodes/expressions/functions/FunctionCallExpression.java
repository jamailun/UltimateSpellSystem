package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A function call statement.
 */
@Getter
public final class FunctionCallExpression extends ExpressionNode {

    private final FunctionDefinition function;
    private final List<ExpressionNode> arguments;

    public FunctionCallExpression(@NotNull TokenPosition position, @NotNull FunctionDefinition function, @NotNull List<ExpressionNode> arguments) {
        super(position);
        this.function = function;
        this.arguments = arguments;
    }

    @Override
    public void validateTypes(@NotNull TypesContext contextRaw) {
        TypesContext context = contextRaw.childContext();

        // Check argument amount
        List<FunctionArgument> listArgs = function.arguments();

        // Then validate arguments types
        for(int i = 0; i < listArgs.size(); i++) {
            FunctionArgument expected = listArgs.get(i);
            ExpressionNode obtained = arguments.get(i);
            obtained.validateTypes(context);

            FunctionType.TestResult result = expected.type().accepts(obtained.getExpressionType());
            if(result.isError())
                throw new TypeException(obtained, "Argument '" + expected.debugName() + "': " + result.getError());
        }
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleFunction(this);
    }

    @Override
    public @NotNull Type getExpressionType() {
        return function.returnedType();
    }

    @PreviousIndicator(expected = TokenType.BRACKET_OPEN)
    public static @NotNull FunctionCallExpression readNextFunctionCall(@NotNull FunctionDefinition functionDefinition, @NotNull TokenStream tokens) {
        List<ExpressionNode> arguments = new ArrayList<>();
        TokenPosition position = tokens.position();

        boolean first = true;
        while(tokens.hasMore()) {
            Token peek = tokens.peek();
            if(peek.getType() == TokenType.BRACKET_CLOSE)
                break;
            if( ! first) {
                tokens.dropOrThrow(TokenType.COMMA);
            } first = false;

            ExpressionNode argument = ExpressionNode.readNextExpression(tokens);
            arguments.add(argument);
        }
        tokens.dropOrThrow(TokenType.BRACKET_CLOSE);

        // Assert obtained arguments count is expected
        if(arguments.size() < functionDefinition.mandatoryArgumentsCount())
            throw new SyntaxException(position, "Invalid arguments count. Expected " + functionDefinition.arguments().size() + " mandatory, got " + arguments.size());
        if(arguments.size() > functionDefinition.arguments().size())
            throw new SyntaxException(position, "Invalid arguments count. Expected " + functionDefinition.arguments().size() + ", got " + arguments.size());

        // Create and return node
        return new FunctionCallExpression(position, functionDefinition, arguments);
    }

    @Override
    public @NotNull String toString() {
        return "F-call{"+function.id()+" (" + arguments + ")}";
    }
}
