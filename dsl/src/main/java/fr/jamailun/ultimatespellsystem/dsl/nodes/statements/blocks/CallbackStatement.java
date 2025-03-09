package fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks;

import fr.jamailun.ultimatespellsystem.dsl.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.variables.VariableDefinition;
import fr.jamailun.ultimatespellsystem.dsl.objects.CallbackEvent;
import fr.jamailun.ultimatespellsystem.dsl.registries.CallbackEventRegistry;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A statement used to trigger an action happens.
 */
@Getter
public class CallbackStatement extends BlockHolder {

    private final TokenPosition firstPosition;
    private final String listenVariableName;
    private final CallbackEvent callbackType;
    private final @Nullable String outputVariable;

    public CallbackStatement(TokenPosition firstPosition, String listenVariableName, CallbackEvent callbackType, @Nullable String outputVariable, StatementNode child) {
        super(child);
        this.firstPosition = firstPosition;
        this.listenVariableName = listenVariableName;
        this.callbackType = callbackType;
        this.outputVariable = outputVariable;
    }

    @Override
    public void validateTypes(@NotNull TypesContext context) {
        // variable must exist and be an entity.
        VariableDefinition input = context.findVariable(listenVariableName);
        if(input == null)
            throw new SyntaxException(firstPosition, "Unknown variable name: '" + listenVariableName + "'.");
        Type inputType = input.getType(context);
        if(!inputType.is(TypePrimitive.ENTITY))
            throw new SyntaxException(firstPosition, "Variable "+listenVariableName+" for a callback must be an entity. Current type is: " + inputType + "'.");
        if(inputType.isCollection())
            throw new SyntaxException(firstPosition, "Variable "+listenVariableName+" for a callback must be an entity, not an array.");

        TypesContext childContext = context.childContext();
        // declare variable (if set)
        if(outputVariable != null) {
            assert callbackType.argument() != null; // compiler helper
            childContext.registerVariable(outputVariable, firstPosition, callbackType.argument().type().asType());
        }

        // Validate child
        child.validateTypes(childContext);
    }

    public Optional<String> getOutputVariable() {
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

        Token eventToken = tokens.next();
        if(eventToken.getType() != TokenType.VALUE_STRING && eventToken.getType() != TokenType.IDENTIFIER) {
            throw new SyntaxException(eventToken, "Invalid token for callback type. Expected a string or an identifier. Got " + eventToken + ".");
        }
        String eventName = eventToken.getContentString();
        CallbackEvent eventType = CallbackEventRegistry.get(eventName);
        if(eventType == null) {
            throw new SyntaxException(eventToken, "Unregistered callback type: '" + eventName + "'.");
        }

        String argVariable;
        if(tokens.dropOptional(TokenType.COLON)) {
            argVariable = null;
        } else {
            if(eventType.argument() == null)
                throw new SyntaxException(tokens.position(), "Callback type " + eventType.name() + " does ot accept any argument. As such, a ':' was expected.");
            if(eventType.argument().keyword() != tokens.peek().getType())
                throw new SyntaxException(tokens.position(), "Callback type " + eventType.name() + " only accept argument with keyword " + eventType.argument().keyword() + ". Got " + tokens.peek() + ".");
            tokens.drop();
            argVariable = tokens.nextOrThrow(TokenType.VALUE_VARIABLE).getContentString();
            tokens.dropOrThrow(TokenType.COLON);
        }

        StatementNode child = StatementNode.parseNextStatement(tokens);
        tokens.dropOptional(TokenType.SEMI_COLON);

        return new CallbackStatement(pos, varName, eventType, argVariable, child);
    }
}
