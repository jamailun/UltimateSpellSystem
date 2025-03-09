package fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * A statement used to trigger an action happens.
 */
@Getter
public class CallbackStatement extends BlockHolder {

    public static final Map<TokenType, TypePrimitive> KEYWORD_TYPE = Map.of(
            TokenType.AT, TypePrimitive.LOCATION,
            TokenType.BY, TypePrimitive.ENTITY
    );

    private final TokenPosition firstPosition;
    private final String listenVariableName;
    private final ExpressionNode eventNode;
    private final @Nullable Output outputVariable;

    public CallbackStatement(TokenPosition firstPosition, String listenVariableName, ExpressionNode eventNode, @Nullable Output outputVariable, StatementNode child) {
        super(child);
        this.firstPosition = firstPosition;
        this.listenVariableName = listenVariableName;
        this.eventNode = eventNode;
        this.outputVariable = outputVariable;
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        TypesContext childContext = context.childContext();
        assertExpressionType(eventNode, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.STRING, TypePrimitive.CUSTOM);
        if(context.findVariable(listenVariableName) == null)
            throw new SyntaxException(firstPosition, "Unknown variable on listen CALLBACK: '" + listenVariableName + "'.");

        // declare variable (if set)
        if(outputVariable != null)
            childContext.registerVariable(outputVariable.varName(), outputVariable.position(), outputVariable.primitive().asType());

        // Validate child
        child.validateTypes(childContext);
    }

    public Optional<Output> getOutputVariable() {
        return Optional.ofNullable(outputVariable);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleCallback(this);
    }

    @PreviousIndicator(expected = TokenType.CALLBACK) // CALLBACK %<VAR> <EVENT> [<keyword> %<ARG>]: {CHILD}
    public static @NotNull CallbackStatement parseCallback(@NotNull TokenStream tokens) {
        TokenPosition pos = tokens.position();
        String varName = tokens.nextOrThrow(TokenType.VALUE_VARIABLE).getContentString();
        ExpressionNode event = ExpressionNode.readNextExpression(tokens, true);

        Output output;
        if(KEYWORD_TYPE.containsKey(tokens.peek().getType())) {
            Token keyword = tokens.next();
            String argVariable = tokens.nextOrThrow(TokenType.VALUE_VARIABLE).getContentString();
            output = Output.of(keyword, argVariable);
        } else {
            if(tokens.peek().getType() != TokenType.COLON)
                throw new SyntaxException(tokens.peek(), "After a callback, either add an arguments, or put a colon (':').");
            output = null;
        }
        tokens.dropOrThrow(TokenType.COLON);

        StatementNode child = StatementNode.parseNextStatement(tokens);
        tokens.dropOptional(TokenType.SEMI_COLON);

        return new CallbackStatement(pos, varName, event, output, child);
    }

    public record Output(@NotNull TokenPosition position, @NotNull TokenType token, @NotNull String varName) {
        public TypePrimitive primitive() {
            return KEYWORD_TYPE.get(token);
        }
        static @NotNull Output of(@NotNull Token token, @NotNull String varName) {
            return new Output(token.pos(), token.getType(), varName);
        }
    }
}
