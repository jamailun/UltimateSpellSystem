package fr.jamailun.ultimatespellsystem.dsl2.validators;

import fr.jamailun.ultimatespellsystem.dsl2.errors.TreeValidationException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.ForLoopStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.IfElseStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.WhileLoopStatement;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.StatementVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Structure validation.
 */
public class StructureValidationVisitor implements StatementVisitor {
    private boolean stopMet = false;
    private boolean breakMet = false;
    private boolean metadataAllowed = true;

    private StructureValidationVisitor child() {
        StructureValidationVisitor clone = new StructureValidationVisitor();
        clone.metadataAllowed = metadataAllowed;
        clone.stopMet = stopMet;
        clone.breakMet = breakMet;
        return clone;
    }

   /* @Override
    public void handleMetadata(@NotNull MetadataStatement statement) {
        if(!metadataAllowed)
            throw new TreeValidationException(TokenPosition.unknown(), "Meta-data can only be the first statements.");
    }*/

    @Override
    public void handleReturn(@NotNull ReturnStatement statement) {
        handleMono();
        stopMet = true;
    }
    @Override
    public void handleBreakContinue(@NotNull BreakContinueStatement statement) {
        handleMono();
        breakMet = true;
    }

    @Override
    public void handleBlock(@NotNull BlockStatement statement) {
        handleMono();
        StructureValidationVisitor child = child();
        statement.getChildren().forEach(n -> n.visit(child));
    }

    private void handleSub(@NotNull StatementNode child) {
        child.visit(child());
    }

    private void handleMono() {
        metadataAllowed = false;
        if(stopMet) {
            throw new TreeValidationException(TokenPosition.unknown(), "Cannot have a statement after a STOP.");
        }
        if(breakMet) {
            throw new TreeValidationException(TokenPosition.unknown(), "Cannot have a statement in the block after a BREAK or a CONTINUE.");
        }
    }

    @Override
    public void handleIf(@NotNull IfElseStatement statement) {
        handleSub(statement.getChild());
        statement.getElse().ifPresent(this::handleSub);
    }
    @Override
    public void handleForLoop(@NotNull ForLoopStatement statement) {
        handleSub(statement.getChild());
    }
    @Override
    public void handleWhileLoop(@NotNull WhileLoopStatement statement) {
        handleSub(statement.getChild());
    }
    @Override
    public void handleDeclareVariable(@NotNull DeclareNewVariableStatement statement) {handleMono();}

    @Override
    public void handleAffectVariable(@NotNull AffectationStatement statement) {
        handleMono();
    }

    @Override
    public void handleFunctionDeclaration(@NotNull FunctionDeclarationStatement statement) {
        //TODO ??
    }

    @Override
    public void handleIncrement(@NotNull IncrementStatement statement) {handleMono();}
    @Override
    public void handleSimpleExpression(@NotNull SimpleExpressionStatement statement) {handleMono();}
}
