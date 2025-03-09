package fr.jamailun.ultimatespellsystem.dsl.validators;

import fr.jamailun.ultimatespellsystem.dsl.errors.TreeValidationException;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.blocks.*;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.visitor.StatementVisitor;
import org.jetbrains.annotations.NotNull;

public class StructureValidationVisitor implements StatementVisitor {
    private StructureValidationVisitor parent;
    private boolean stopMet = false;
    private boolean breakMet = false;
    private boolean metadataAllowed = true;

    public StructureValidationVisitor child() {
        StructureValidationVisitor clone = new StructureValidationVisitor();
        clone.metadataAllowed = metadataAllowed;
        clone.stopMet = stopMet;
        clone.breakMet = breakMet;
        clone.parent = this;
        return clone;
    }

    public StructureValidationVisitor root() {
        return parent == null ? this : parent;
    }

    @Override
    public void handleMetadata(@NotNull MetadataStatement statement) {
        if(!metadataAllowed)
            throw new TreeValidationException(TokenPosition.unknown(), "Meta-data can only be the first statements.");
    }

    @Override
    public void handleStop(@NotNull StopStatement statement) {
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
    public void handleForeachLoop(@NotNull ForeachLoopStatement statement) {
        handleSub(statement.getChild());
    }
    @Override
    public void handleWhileLoop(@NotNull WhileLoopStatement statement) {
        handleSub(statement.getChild());
    }
    @Override
    public void handleRunLater(@NotNull RunLaterStatement statement) {
        statement.getChild().visit(child());
    }
    @Override
    public void handleRepeatRun(@NotNull RepeatStatement statement) {statement.getChild().visit(child());}
    @Override
    public void handleCallback(@NotNull CallbackStatement statement) {statement.getChild().visit(child());}

    @Override
    public void handleSendMessage(@NotNull SendMessageStatement statement) {handleMono();}
    @Override
    public void handleSendEffect(@NotNull SendEffectStatement statement) {handleMono();}
    @Override
    public void handleSendAttribute(@NotNull SendAttributeStatement statement) {handleMono();}

    @Override
    public void handleDefine(@NotNull DefineStatement statement) {handleMono();}
    @Override
    public void handleSummon(@NotNull SummonStatement statement) {handleMono();}
    @Override
    public void handleIncrement(@NotNull IncrementStatement statement) {handleMono();}
    @Override
    public void handleTeleport(@NotNull TeleportStatement statement) {handleMono();}
    @Override
    public void handlePlay(@NotNull PlayStatement statement) {handleMono();}
    @Override
    public void handleGive(@NotNull GiveStatement statement) {handleMono();}
    @Override
    public void handleSimpleExpression(@NotNull SimpleExpressionStatement statement) {handleMono();}
}
