package fr.jamailun.ultimatespellsystem.dsl2.library.structs;

import fr.jamailun.ultimatespellsystem.dsl2.library.StructDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;

public class ConsoleStruct extends StructDefinition {

  public static final String NAME = "console";

  public ConsoleStruct() {
    super(NAME);
    initFunctions();
  }

  private void initFunctions() {
    registerFunction(FunctionDefinition.of(
        "send",
        Type.NULL,
        FunctionArgument.of(TypePrimitive.STRING)
    ));
    registerFunction(FunctionDefinition.of(
        "info",
        Type.NULL,
        FunctionArgument.of(TypePrimitive.STRING)
    ));
    registerFunction(FunctionDefinition.of(
        "debug",
        Type.NULL,
        FunctionArgument.of(TypePrimitive.STRING)
    ));
    registerFunction(FunctionDefinition.of(
        "warning",
        Type.NULL,
        FunctionArgument.of(TypePrimitive.STRING)
    ));
    registerFunction(FunctionDefinition.of(
        "error",
        Type.NULL,
        FunctionArgument.of(TypePrimitive.STRING)
    ));
  }

}
