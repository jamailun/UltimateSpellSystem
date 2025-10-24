package fr.jamailun.ultimatespellsystem.dsl2.registries;

import fr.jamailun.ultimatespellsystem.dsl2.library.StructDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ObjectsDefinitionRegistry {
  private ObjectsDefinitionRegistry() {/* private */}

  private static final Map<String, StructDefinition> DEFAULT_STRUCTS = new HashMap<>();

  public static void registerDefaultStruct(@NotNull StructDefinition definition) {
    DEFAULT_STRUCTS.put(definition.getName(), definition);
  }

  public static @NotNull @UnmodifiableView Collection<StructDefinition> getDefaultStructs() {
    return Collections.unmodifiableCollection(DEFAULT_STRUCTS.values());
  }

}
