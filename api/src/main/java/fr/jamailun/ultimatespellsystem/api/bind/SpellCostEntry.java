package fr.jamailun.ultimatespellsystem.api.bind;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

/**
 * An entry to define a new spell-cost.
 * @param id the non-null ID used to <b>persist</b> the cost itself. Will also be used for commands.
 * @param clazz the serialized class.
 * @param deserializer a function to transform a list of serialized elements to the spell instance.
 * @param args expected arguments list with commands.
 * @param <T> the output spell cost class.
 */
public record SpellCostEntry<T extends SpellCost>(
    @NotNull String id,
    @NotNull Class<T> clazz,
    @NotNull Function<List<String>, T> deserializer,
    @NotNull List<SpellCostArgType> args
) {
  /**
   *
   * @param id the ID of the entry.
   * @param deserializer deserializer function.
   * @param args command arguments
   * @return a new instance
   */
  @Contract("_,_,_,_ -> new")
  public static <T extends SpellCost> @NotNull SpellCostEntry<T> of(@NotNull String id, @NotNull Class<T> clazz, @NotNull Function<List<String>, T> deserializer, @NotNull SpellCostArgType... args) {
    return new SpellCostEntry<>(id, clazz, deserializer, List.of(args));
  }

  /**
   * Use the {@link #deserializer} function.
   * @param args arguments to pass to the deserializer.
   * @return a new {@link SpellCost} instance.
   */
  public @NotNull SpellCost deserialize(List<String> args) {
    return deserializer.apply(args);
  }
}
