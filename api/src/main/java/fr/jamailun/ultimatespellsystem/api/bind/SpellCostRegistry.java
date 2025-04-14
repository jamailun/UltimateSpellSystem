package fr.jamailun.ultimatespellsystem.api.bind;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

/**
 * Factory for spell costs.
 */
public interface SpellCostRegistry {

    /**
     * Register a new spell-cost entry.
     * @param entry a non-null instance. The {@link SpellCostEntry#id()} must return a <b>unique</b> identifier.
     */
    void register(@NotNull SpellCostEntry<?> entry);

    /**
     * List registered costs IDs.
     * @return a non-null and non-mutable collection of strings.
     */
    @NotNull @UnmodifiableView Collection<String> listIds();

    /**
     * Get a {@link SpellCostEntry} from its id.
     * @param id a non-null ID.
     * @return null if nothing was found.
     */
    @Nullable SpellCostEntry<?> get(@NotNull String id);

    /**
     * Find an entry from its class.
     * @param clazz the spell cost class.
     * @return null if nothing was found.
     */
    @Nullable SpellCostEntry<?> getByClass(@NotNull Class<? extends SpellCost> clazz);

}
