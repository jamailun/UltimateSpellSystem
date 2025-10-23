package fr.jamailun.ultimatespellsystem.dsl2.ast;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.ParenthesisExpression;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators.BiOperator;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators.MonoOperator;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.operators.Operator;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.ForLoopStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.IfElseStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.blocks.WhileLoopStatement;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.StatementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Fold constants in the AST.
 */
public class ConstantFoldingOptimizer extends ASTOptimizer implements StatementVisitor {

  @Override
  public void optimize(@NotNull StatementNode statement) {
    statement.visit(this);
  }

  private void foldConstants(@Nullable ExpressionNode expression) {

  }

  private @Nullable Double foldConstants(@Nullable BiOperator biOperator) {
    if(biOperator == null) {
      return null;
    }
    //TODO !
    throw new UnsupportedOperationException("todo");
  }

  private @Nullable Double foldNumericConstants(@Nullable ExpressionNode expression) {
    return switch (expression) {
      case MapLiteral mapLiteral -> {
        mapLiteral.getExpressions().values().forEach(this::foldNumericConstants);
        yield null;
      }
      case ArrayLiteral arrayLiteral -> {
        arrayLiteral.getElements().forEach(this::foldNumericConstants);
        yield null;
      }
      case ParenthesisExpression pe -> foldNumericConstants(pe.getExpression());
      case NumberLiteral n -> n.getRaw();
      case BiOperator biOp -> {
        Double left = foldNumericConstants(biOp.getLeft());
        Double right = foldNumericConstants(biOp.getRight());
        if(left != null && right != null) {

        }
        yield null;
      }
      case null, default -> null;
    };
  }

  private boolean isConstant(@Nullable ExpressionNode expression) {
    return switch (expression) {
      case MapLiteral mapLiteral -> mapLiteral.getExpressions().values().stream().allMatch(this::isConstant);
      case ArrayLiteral arrayLiteral -> arrayLiteral.getElements().stream().allMatch(this::isConstant);
      case LiteralExpression<?> ignored -> true;
      case LocationLiteral locLiteral ->
          isConstant(locLiteral.getVectorX()) &&
          isConstant(locLiteral.getVectorY()) &&
          isConstant(locLiteral.getVectorZ()) &&
          isConstant(locLiteral.getWorld()) &&
          isConstant(locLiteral.getPitch()) &&
          isConstant(locLiteral.getYaw());
      case BiOperator biOp -> isConstant(biOp.getLeft()) && isConstant(biOp.getRight());
      case MonoOperator monoOp -> isConstant(monoOp.getChild());
      case ParenthesisExpression pe -> isConstant(pe.getExpression());
      case null, default -> false;
    };
  }

  // -- visitor

  @Override
  public void handleDeclareVariable(@NotNull DeclareNewVariableStatement statement) {
    if(statement.getExpression() != null) {

    }
  }

  @Override
  public void handleAffectVariable(@NotNull AffectationStatement statement) {
    statement.getExpression();
  }

  @Override
  public void handleReturn(@NotNull ReturnStatement statement) {
    if(statement.getExitCodeNode() != null) {

    }
  }

  @Override
  public void handleBlock(@NotNull BlockStatement statement) {
    statement.getChildren().forEach(this::optimize);
  }

  @Override
  public void handleSimpleExpression(@NotNull SimpleExpressionStatement statement) {

  }

  @Override
  public void handleIf(@NotNull IfElseStatement statement) {

  }

  @Override
  public void handleForLoop(@NotNull ForLoopStatement statement) {

  }

  @Override
  public void handleWhileLoop(@NotNull WhileLoopStatement statement) {

  }

  // rien

  @Override
  public void handleIncrement(@NotNull IncrementStatement statement) {
    // rien
  }

  @Override
  public void handleFunctionDeclaration(@NotNull FunctionDeclarationStatement statement) {
    // rien
  }

  @Override
  public void handleBreakContinue(@NotNull BreakContinueStatement statement) {
    // rien
  }
}
