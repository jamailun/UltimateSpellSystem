package fr.jamailun.ultimatespellsystem.dsl2.registries;

import fr.jamailun.ultimatespellsystem.dsl2.library.StructDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Register for all default {@link StructDefinition}.
 */
public final class ObjectsDefinitionRegistry {
  private ObjectsDefinitionRegistry() {/* private */}

  private static final Map<String, StructDefinition> DEFAULT_STRUCTS = new HashMap<>();

  /**
   * Register a new struct.
   * @param definition non-null definition to use.
   */
  public static void registerDefaultStruct(@NotNull StructDefinition definition) {
    DEFAULT_STRUCTS.put(definition.getName(), definition);
  }

  /**
   * Get the registered names.
   * @return a non-null view on the IDs.
   */
  public static @NotNull @UnmodifiableView Collection<String> getDefaultStructsNames() {
    return Collections.unmodifiableCollection(DEFAULT_STRUCTS.keySet());
  }

  /**
   * Get the registered structs.
   * @return a non-null view on the values.
   */
  public static @NotNull @UnmodifiableView Collection<StructDefinition> getDefaultStructs() {
    return Collections.unmodifiableCollection(DEFAULT_STRUCTS.values());
  }

}
