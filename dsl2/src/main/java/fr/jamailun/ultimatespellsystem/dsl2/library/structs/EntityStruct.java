package fr.jamailun.ultimatespellsystem.dsl2.library.structs;

import fr.jamailun.ultimatespellsystem.dsl2.library.StructDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;

public class EntityStruct extends StructDefinition {

  public static final String NAME = "entity";

  public EntityStruct() {
    super(NAME);
    initFields();
    initFunctions();
  }

  private void initFields() {
    registerField("name", TypePrimitive.STRING);

    registerField("location", LAZY_TYPE_LOCATION);
    registerField("eye_location", LAZY_TYPE_LOCATION);

    // Location
    registerField("x", TypePrimitive.NUMBER);
    registerField("y", TypePrimitive.NUMBER);
    registerField("z", TypePrimitive.NUMBER);
    registerField("yaw", TypePrimitive.NUMBER);
    registerField("pitch", TypePrimitive.NUMBER);

    // Attributes
    registerField("health", TypePrimitive.NUMBER);
    registerField("max_health", TypePrimitive.NUMBER);
  }

  private void initFunctions() {
    registerFunction(FunctionDefinition.of(
        "teleport",
        TypePrimitive.NULL.asType(),
        FunctionArgument.of(LAZY_TYPE_LOCATION)
    ));

    registerFunction(FunctionDefinition.of(
        "heal",
        TypePrimitive.NULL.asType(),
        FunctionArgument.of(TypePrimitive.NUMBER)
    ));

    registerFunction(FunctionDefinition.of(
        "send_message",
        TypePrimitive.NULL.asType(),
        FunctionArgument.of(TypePrimitive.STRING)
    ));
  }

}
