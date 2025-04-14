package fr.jamailun.ultimatespellsystem.plugin.bind;

import fr.jamailun.ultimatespellsystem.api.bind.SpellBindData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Container for {@link SpellBindData}.
 */
public record SpellBindDataContainer(@NotNull List<SpellBindData> list) {
}
