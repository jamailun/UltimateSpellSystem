package fr.jamailun.ultimatespellsystem.dsl2.validators;

import fr.jamailun.ultimatespellsystem.dsl2.errors.TreeValidationException;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.ForLoopStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.IfElseStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.WhileLoopStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.StatementVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Structure validation.
 */
public class StructureValidationVisitor implements StatementVisitor {

    private TypeRef returnType = new TypeRef(); // reference
    private boolean returnMet = false;
    private boolean breakMet = false;
    private boolean metadataAllowed = true;

    private @NotNull StructureValidationVisitor child() {
        StructureValidationVisitor clone = new StructureValidationVisitor();
        clone.metadataAllowed = metadataAllowed;
        clone.returnMet = returnMet;
        clone.breakMet = breakMet;
        clone.returnType = returnType; // copy ref
        return clone;
    }

    @Override
    public void handleReturn(@NotNull ReturnStatement statement) {
        handleMono();
        returnMet = true;
        if(returnType.type != null && !Objects.equals(returnType.type, statement.getReturnType())) {
            throw new TreeValidationException(statement.getPos(), "Cannot use a return of type " + statement.getReturnType() + " : another return in the same scoped returned a " + returnType.type);
        }
        returnType.type = statement.getReturnType();
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

    @Override
    public void handleFunctionDeclaration(@NotNull FunctionDeclarationStatement statement) {
        // Prepare child context
        StructureValidationVisitor child = child();
        child.returnType = new TypeRef();

        // Apply to statements
        statement.getStatements().forEach(n -> n.visit(child));

        // Check returns
        if(!statement.getOutputType().isNull()) {
            // No return
            if(!child.returnMet)
                throw new TreeValidationException(statement.getPosition(), "Function " + statement.getFunctionName() + " should return " + statement.getFunctionReturnType() + ", but not all branches return a value.");
            // Different return type.
            if(!Objects.equals(statement.getOutputType(), child.returnType.type))
                throw new TreeValidationException(statement.getPosition(), "Function " + statement.getFunctionName() + " should return " + statement.getFunctionReturnType() + ", but returned "+child.returnType.type+" instead.");
        }
    }

    private void handleSub(@NotNull StatementNode child) {
        child.visit(child());
    }

    private void handleMono() {
        metadataAllowed = false;
        if(returnMet) {
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
    public void handleSimpleExpression(@NotNull SimpleExpressionStatement statement) {handleMono();}

    private static class TypeRef {
        Type type;
    }
}
