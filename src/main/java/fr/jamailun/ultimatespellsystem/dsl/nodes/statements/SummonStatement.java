package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SummonStatement extends StatementNode {

    private final ExpressionNode entityType;
    private final ExpressionNode optSource; // nullable
    private final Token optVarName; // nullable
    private final ExpressionNode duration;
    private final ExpressionNode optProperties; // nullable

    public SummonStatement(ExpressionNode entityType, ExpressionNode optSource, Token varName, ExpressionNode duration, ExpressionNode properties) {
        this.entityType = entityType;
        this.optSource = optSource;
        this.optVarName = varName;
        this.duration = duration;
        this.optProperties = properties;
    }

    @Override
    public void validateTypes(TypesContext context) {
        assertExpressionType(entityType, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.ENTITY_TYPE);
        assertExpressionType(duration,  CollectionFilter.MONO_ELEMENT, context, TypePrimitive.DURATION);
        if(optProperties != null)
            assertExpressionType(optProperties, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.PROPERTIES_SET);

        // Register varName
        if(optVarName != null) {
            context.registerVariable(optVarName.getContentString(), optVarName.pos(), TypePrimitive.ENTITY_TYPE.asType());
        }
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleSummon(this);
    }

    // SUMMON (ENTITY_TYPE) [AT (POSITION/ENTITY)] [[AS (VAR_NAME)]] FOR (DURATION) [WITH : (PROPS)]
    @PreviousIndicator(expected = {TokenType.SUMMON})
    public static SummonStatement parseSummonStatement(TokenStream tokens) {
        //IRON_GOLEM as %ig for 10 seconds with:
        ExpressionNode entityType = ExpressionNode.readNextExpression(tokens);

        ExpressionNode source = null;
        if(tokens.dropOptional(TokenType.AT)) {
            source = ExpressionNode.readNextExpression(tokens);
        }

        // AS (VARIABLE)
        Token varName = null;
        if(tokens.dropOptional(TokenType.AS)) {
            varName = tokens.nextOrThrow(TokenType.VALUE_VARIABLE);
        }

        tokens.dropOrThrow(TokenType.FOR);;
        ExpressionNode duration = ExpressionNode.readNextExpression(tokens);

        ExpressionNode optProperties = null;
        if(tokens.dropOptional(TokenType.WITH)) {
            tokens.dropOrThrow(TokenType.COLON);
            optProperties = ExpressionNode.readNextExpression(tokens);
        }

        tokens.dropOptional(TokenType.SEMI_COLON);

        return new SummonStatement(entityType, source, varName, duration, optProperties);
    }

    @Override
    public String toString() {
        return "SUMMON{" + entityType
                + (optSource != null ? " AT " + optSource : "")
                + (optVarName != null ? " AS %" + optVarName.getContentString() : "")
                + " FOR " + duration
                + (optProperties !=null?" WITH " + optProperties : "")
                + "}";
    }

    public ExpressionNode getEntityType() {
        return entityType;
    }

    public Optional<String> getVarName() {
        return optVarName == null ? Optional.empty() : Optional.of(optVarName.getContentString());
    }

    public ExpressionNode getDuration() {
        return duration;
    }

    public Optional<ExpressionNode> getProperties() {
        return Optional.ofNullable(optProperties);
    }

    public Optional<ExpressionNode> getSource() {
        return Optional.ofNullable(optSource);
    }
}
