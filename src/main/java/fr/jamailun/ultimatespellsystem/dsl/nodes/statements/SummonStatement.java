package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;

import java.util.Optional;

public class SummonStatement extends StatementNode {

    private final ExpressionNode entityType;
    private final Token varName; // nullable
    private final ExpressionNode duration;
    private final ExpressionNode properties; // nullable

    public SummonStatement(ExpressionNode entityType, Token varName, ExpressionNode duration, ExpressionNode properties) {
        this.entityType = entityType;
        this.varName = varName;
        this.duration = duration;
        this.properties = properties;
    }

    @Override
    public void validateTypes(TypesContext context) {
        assertExpressionType(entityType, context, TypePrimitive.ENTITY_TYPE);
        assertExpressionType(duration, context, TypePrimitive.DURATION);
        if(properties != null)
            assertExpressionType(properties, context, TypePrimitive.PROPERTIES_SET);

        // Register varName
        if(varName != null) {
            context.registerVariable(varName.getContentString(), varName.pos(), TypePrimitive.ENTITY_TYPE.asType());
        }
    }

    @Override
    public void visit(StatementVisitor visitor) {
        visitor.handleSummon(this);
    }

    // SUMMON (ENTITY_TYPE) [[AS (VAR_NAME)]] FOR (DURATION) [WITH : (PROPS)]
    @PreviousIndicator(expected = {TokenType.SUMMON})
    public static SummonStatement parseSummonStatement(TokenStream tokens) {
        //IRON_GOLEM as %ig for 10 seconds with:
        ExpressionNode entityType = ExpressionNode.readNextExpression(tokens);

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

        return new SummonStatement(entityType, varName, duration, optProperties);
    }

    @Override
    public String toString() {
        return "SUMMON{" + entityType
                + (varName != null ? " AS %" + varName.getContentString() : "")
                + " FOR " + duration
                + (properties!=null?" WITH " + properties : "")
                + "}";
    }

    public ExpressionNode getEntityType() {
        return entityType;
    }

    public Optional<String> getVarName() {
        return varName == null ? Optional.empty() : Optional.of(varName.getContentString());
    }

    public ExpressionNode getDuration() {
        return duration;
    }

    public Optional<ExpressionNode> getProperties() {
        return Optional.ofNullable(properties);
    }
}
