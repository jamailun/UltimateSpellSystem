package fr.jamailun.ultimatespellsystem.dsl2.library.structs;

import fr.jamailun.ultimatespellsystem.dsl2.library.StructDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;

public class EntityStruct extends StructDefinition {

  public static final String NAME = TypePrimitive.ENTITY.name().toLowerCase();

  public EntityStruct() {
    super(NAME);
    initFields();
    initFunctions();
  }

  private void initFields() {
    registerField("name", TypePrimitive.STRING);

    registerField("location", TypePrimitive.LOCATION);
    registerField("eye_location", TypePrimitive.LOCATION);

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
        Type.NULL,
        FunctionArgument.of(TypePrimitive.LOCATION)
    ));

    registerFunction(FunctionDefinition.of(
        "heal",
        Type.NULL,
        FunctionArgument.of(TypePrimitive.NUMBER)
    ));

    registerFunction(FunctionDefinition.of(
        "send_message",
        Type.NULL,
        FunctionArgument.of(TypePrimitive.STRING)
    ));
  }

}
