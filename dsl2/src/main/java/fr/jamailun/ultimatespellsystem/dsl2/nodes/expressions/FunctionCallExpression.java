package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.ExpressionVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class FunctionCallExpression extends ExpressionNode {

  private final ExpressionNode caller;
  private final String functionName;
  private final List<ExpressionNode> arguments;

  public FunctionCallExpression(@NotNull ExpressionNode caller, @NotNull String functionName, @NotNull List<ExpressionNode> arguments) {
    super(caller.firstTokenPosition());
    this.caller = caller;
    this.functionName = functionName;
    this.arguments = arguments;
  }

  @Override
  public @NotNull Type getExpressionType() {
    // Pas facile : il faut un registre de définition.
    // Ne peut être su qu'après avoir défini toutes les "classes".
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void validateTypes(@NotNull TypesContext context) {
    // 1. On vérifie que nos arguments existent :)
    for(ExpressionNode argument : arguments) {
      argument.validateTypes(context.childContext());
    }
  }

  @Override
  public void visit(@NotNull ExpressionVisitor visitor) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
