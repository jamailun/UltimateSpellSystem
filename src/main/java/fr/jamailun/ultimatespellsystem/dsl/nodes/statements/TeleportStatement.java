package fr.jamailun.ultimatespellsystem.dsl.nodes.statements;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.CollectionFilter;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.PreviousIndicator;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenStream;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import org.jetbrains.annotations.NotNull;

public class TeleportStatement extends StatementNode {

    private final ExpressionNode entity;
    private final ExpressionNode target;

    public TeleportStatement(ExpressionNode entity, ExpressionNode target) {
        this.entity = entity;
        this.target = target;
    }

    public ExpressionNode getEntity() {
        return entity;
    }

    public ExpressionNode getTarget() {
        return target;
    }

    @Override
    public void validateTypes(TypesContext context) {
        assertExpressionType(entity, context, TypePrimitive.ENTITY);
        assertExpressionType(target, CollectionFilter.MONO_ELEMENT, context, TypePrimitive.ENTITY, TypePrimitive.LOCATION);
    }

    @Override
    public void visit(@NotNull StatementVisitor visitor) {
        visitor.handleTeleport(this);
    }

    @PreviousIndicator(expected = TokenType.TELEPORT)
    public static TeleportStatement parseTeleport(TokenStream tokens) {
        ExpressionNode entity = ExpressionNode.readNextExpression(tokens);
        tokens.dropOrThrow(TokenType.TO);
        ExpressionNode target = ExpressionNode.readNextExpression(tokens);
        tokens.dropOptional(TokenType.SEMI_COLON);
        return new TeleportStatement(entity, target);
    }

    @Override
    public String toString() {
        return "TELEPORT " + entity + " TO " + target;
    }
}
