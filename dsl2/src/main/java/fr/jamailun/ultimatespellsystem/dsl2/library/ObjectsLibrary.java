package fr.jamailun.ultimatespellsystem.dsl2.library;

import fr.jamailun.ultimatespellsystem.dsl2.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.statements.FunctionDeclarationStatement;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl2.registries.ObjectsDefinitionRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.SequencedCollection;

public class ObjectsLibrary {

  private final Map<String, FunctionDeclarationStatement> functions = new HashMap<>();
  private final Map<String, StructDefinition> structs = new HashMap<>();
  private final Map<String, Type> types = new HashMap<>();

  public ObjectsLibrary() {
    this(true);
  }

  public ObjectsLibrary(boolean registerDefaults) {
    types.putAll(TypePrimitive.getTypesMap());
    if(registerDefaults) {
      ObjectsDefinitionRegistry.getDefaultStructs().forEach(this::registerStruct);
    }
  }

  public void registerStruct(@NotNull StructDefinition struct) {
    structs.put(struct.getName(), struct);
    types.put(struct.getName(), struct.asType());
  }

  public void registerStatements(@NotNull SequencedCollection<StatementNode> statements) {
    for(StatementNode statement : statements) {
      if(statement instanceof FunctionDeclarationStatement fda) {
        functions.put(fda.getFunctionName(), fda);
      }
    }
  }

  public @Nullable FunctionDeclarationStatement getFunction(String name) {
    return functions.get(name);
  }

  public @Nullable StructDefinition getStruct(String name) {
    return structs.get(name);
  }

  public @Nullable Type getType(String name) {
    return types.get(name);
  }

}
