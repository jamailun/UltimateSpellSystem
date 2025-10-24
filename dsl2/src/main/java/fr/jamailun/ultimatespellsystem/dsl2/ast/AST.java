package fr.jamailun.ultimatespellsystem.dsl2.ast;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.FunctionDeclarationStatement;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AST implements Iterable<StatementNode> {

  private final List<FunctionDeclarationStatement> functionDeclarationStatements = new ArrayList<>();
  private final List<StatementNode> statements = new ArrayList<>();

  /**
   * Create a copy of a statements in the AST.
   * @param statements a collection to copy.
   */
  public AST(@NotNull SequencedCollection<StatementNode> statements) {
    for(StatementNode statement : statements) {
      if(statement instanceof FunctionDeclarationStatement fda) {
        functionDeclarationStatements.add(fda);
      } else {
        this.statements.add(statement);
      }
    }
  }

  /**
   * Get a mutable copy of the statements.
   * @return a mutable list.
   */
  public @NotNull List<StatementNode> getStatements() {
    return new ArrayList<>(statements);
  }

  @Override
  public @NotNull Iterator<StatementNode> iterator() {
    return statements.listIterator();
  }

  @Override
  public Spliterator<StatementNode> spliterator() {
    return statements.spliterator();
  }
}
