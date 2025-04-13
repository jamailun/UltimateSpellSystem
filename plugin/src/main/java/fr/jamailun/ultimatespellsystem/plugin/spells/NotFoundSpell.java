package fr.jamailun.ultimatespellsystem.plugin.spells;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.spells.SpellMetadata;
import fr.jamailun.ultimatespellsystem.api.utils.MultivaluedMap;
import org.jetbrains.annotations.NotNull;

/**
 * For a spell that could not be found.
 */
public class NotFoundSpell extends AbstractSpell {
  public NotFoundSpell(@NotNull String name) {
    super(name);
  }

  @Override
  protected boolean castSpell(@NotNull SpellEntity caster, @NotNull SpellRuntime runtime) {
    return false;
  }

  @Override
  public @NotNull MultivaluedMap<String, SpellMetadata> getMetadata() {
    return new MultivaluedMap<>();
  }

  @Override
  public @NotNull String getDebugString() {
    return "<NOT_FOUND("+name+")>";
  }
}
