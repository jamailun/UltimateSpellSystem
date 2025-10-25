package fr.jamailun.ultimatespellsystem.dsl2.library.structs;

import fr.jamailun.ultimatespellsystem.dsl2.library.StructDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionDefinition;
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
        TypePrimitive.NULL.asType(),
        FunctionArgument.of(TypePrimitive.STRING)
    ));
    registerFunction(FunctionDefinition.of(
        "info",
        TypePrimitive.NULL.asType(),
        FunctionArgument.of(TypePrimitive.STRING)
    ));
    registerFunction(FunctionDefinition.of(
        "debug",
        TypePrimitive.NULL.asType(),
        FunctionArgument.of(TypePrimitive.STRING)
    ));
    registerFunction(FunctionDefinition.of(
        "warning",
        TypePrimitive.NULL.asType(),
        FunctionArgument.of(TypePrimitive.STRING)
    ));
    registerFunction(FunctionDefinition.of(
        "error",
        TypePrimitive.NULL.asType(),
        FunctionArgument.of(TypePrimitive.STRING)
    ));
  }

}
