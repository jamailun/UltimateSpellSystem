package fr.jamailun.ultimatespellsystem.dsl2.ast;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

/**
 * Stateful optimizer
 */
public abstract class ASTOptimizer {

  /**
   * Optimize a single statement.
   * @param statement statement to optimize.
   */
  public abstract void optimize(@NotNull StatementNode statement);

  /**
   * DO a full optimization of the AST.
   * @param ast tree to optimize.
   */
  public final void optimize(@NotNull AST ast) {
    preOptimize();
    for(StatementNode statement : ast) {
      optimize(statement);
    }
    postOptimize();
  }

  protected void preOptimize() {
    // Rien
  }

  protected void postOptimize() {
    // Rien
  }

}
