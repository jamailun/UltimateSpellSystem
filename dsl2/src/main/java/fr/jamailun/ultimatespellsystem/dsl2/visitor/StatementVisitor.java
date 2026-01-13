package fr.jamailun.ultimatespellsystem.dsl2.visitor;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.ForLoopStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.IfElseStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.WhileLoopStatement;
import org.jetbrains.annotations.NotNull;

/**
 * A visitor. Can interact with the node-tree.
 */
public interface StatementVisitor {

    void handleDeclareVariable(@NotNull DeclareNewVariableStatement statement);
    void handleAffectVariable(@NotNull AffectationStatement statement);
    void handleFunctionDeclaration(@NotNull FunctionDeclarationStatement statement);

    void handleReturn(@NotNull ReturnStatement statement);
    void handleBlock(@NotNull BlockStatement statement);
    void handleSimpleExpression(@NotNull SimpleExpressionStatement statement);

    void handleIf(@NotNull IfElseStatement statement);
    void handleForLoop(@NotNull ForLoopStatement statement);
    //void handleForeachLoop(@NotNull ForeachLoopStatement statement);
    void handleWhileLoop(@NotNull WhileLoopStatement statement);
    void handleBreakContinue(@NotNull BreakContinueStatement statement);
}
