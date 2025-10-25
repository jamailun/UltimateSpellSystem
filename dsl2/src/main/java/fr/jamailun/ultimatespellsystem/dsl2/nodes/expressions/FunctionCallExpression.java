package fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions;

import fr.jamailun.ultimatespellsystem.dsl2.errors.SyntaxException;
import fr.jamailun.ultimatespellsystem.dsl2.errors.TypeException;
import fr.jamailun.ultimatespellsystem.dsl2.library.StructDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.FunctionDeclarationStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.variables.TypesContext;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl2.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl2.visitor.ExpressionVisitor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@Getter
public class FunctionCallExpression extends ExpressionNode {

  private Type runtimeType = null;

  private final TokenPosition position;
  private final @Nullable ExpressionNode caller;
  private final String functionName;
  private final List<ExpressionNode> arguments;

  public FunctionCallExpression(@Nullable ExpressionNode caller, @NotNull Token functionName, @NotNull List<ExpressionNode> arguments) {
    super(functionName.pos());
    this.caller = caller;
    this.position = functionName.pos();
    this.functionName = functionName.getContentString();
    this.arguments = arguments;
  }

  @Override
  public @NotNull Type getExpressionType() {
    assert runtimeType != null;
    return runtimeType;
  }

  @Override
  public void validateTypes(@NotNull TypesContext context) {
    // 1. Propagate to arguments
    for(ExpressionNode argument : arguments) {
      argument.validateTypes(context.childContext());
    }

    // 2. If we have a caller : we check the function from the type definition.
    FunctionDefinition function;
    if(caller != null) {
      caller.validateTypes(context);
      Type callerType = caller.getExpressionType();
      StructDefinition struct = context.findStruct(callerType);
      if(struct == null) {
        throw new TypeException(position, "Unknown struct for type " + callerType);
      }
      function = struct.getFunction(functionName);
      if(function == null) {
        throw new TypeException(position, "Cannot call function '" + functionName + "' on type " + callerType + ".");
      }
    }
    // Pas de caller : function globale
    else {
      FunctionDeclarationStatement declaration = context.findFunction(functionName);
      if(declaration == null) {
        throw new SyntaxException(position, "Function '" + functionName + "' not found");
      }
      function = declaration.asFunctionDefinition();
    }

    // 3. Set our type
    this.runtimeType = function.returnedType();

    // 4. Check arguments list match requested types
    if(arguments.size() != function.arguments().size()) {
      throw new SyntaxException(position, "Function argument count mismatch. Expected " + function.arguments().size() + " but got " + arguments.size() + ".");
    }
    for(int i = 0; i < arguments.size(); i++) {
      ExpressionNode arg = arguments.get(i);
      FunctionArgument def = function.arguments().get(i);
      if(!Objects.equals(arg.getExpressionType(), def.type())) {
        throw new TypeException(arg.firstTokenPosition(), "Bad argument type. Function " + functionName + " expected " + def.type() + " on argument n°" + (i+1) + ", but got " + arg.getExpressionType() + ".");
      }
    }
  }

  @Override
  public void visit(@NotNull ExpressionVisitor visitor) {
    visitor.handleFunctionCall(this);
  }

  @Override
  public String toString() {
    return (caller == null ? "" : caller + ".") + functionName + "(" + arguments + ")";
  }
}
