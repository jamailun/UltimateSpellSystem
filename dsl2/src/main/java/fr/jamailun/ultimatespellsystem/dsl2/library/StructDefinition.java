package fr.jamailun.ultimatespellsystem.dsl2.library;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionDefinition;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class StructDefinition {

  protected static final String LAZY_TYPE_LOCATION = "location";

  @Getter private final String name;
  private final Map<String, Type> fields = new HashMap<>();
  private final Map<String, FunctionDefinition> functions = new HashMap<>();

  public void registerField(@NotNull String fieldName, @NotNull Type type) {
    fields.put(fieldName, type);
  }

  public void registerField(@NotNull String fieldName, @NotNull TypePrimitive primitive) {
    fields.put(fieldName, new Type(primitive, 0));
  }

  protected void registerField(@NotNull String fieldName, @NotNull String lazyType) {
    fields.put(fieldName, new Type(lazyType, 0));
  }

  public void registerFunction(@NotNull FunctionDefinition function) {
    functions.put(function.id(), function);
  }

  public @Nullable Type getFieldType(@NotNull String fieldName) {
    return fields.get(fieldName);
  }

  public @Nullable FunctionDefinition getFunction(@NotNull String functionName) {
    return functions.get(functionName);
  }

  public @NotNull Type asType() {
    return new Type(name, 0);
  }

}
