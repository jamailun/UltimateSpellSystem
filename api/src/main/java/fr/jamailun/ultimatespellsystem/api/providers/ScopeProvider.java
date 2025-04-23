package fr.jamailun.ultimatespellsystem.api.providers;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.EntityScope;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provider for {@link EntityScope} : predicates on {@link Entity}.
 */
public final class ScopeProvider extends UssProvider<EntityScope> {
    private ScopeProvider() {}
    private static final ScopeProvider INSTANCE = new ScopeProvider();

    /**
     * Get the provider instance.
     * @return a non-null instance.
     */
    public static @NotNull ScopeProvider instance() {
        return INSTANCE;
    }

    // Load custom scopes
    static {
        instance().register(e -> e instanceof LivingEntity, "living", "living_entity", "living_entities", "all", "any");
        instance().register(e -> e instanceof Mob || e.getScoreboardTags().contains("mob"), "mob");
        instance().register(e -> e instanceof Monster || e.getScoreboardTags().contains("monster"), "monster");
        instance().register(e -> e instanceof Animals || e.getScoreboardTags().contains("animal"), "animal");
        instance().register(e -> e instanceof Player, "player", "human");
        instance().register(e -> e instanceof Item, "item");
        instance().register(e -> UltimateSpellSystem.getSummonsManager().isASummonedEntity(e.getUniqueId()), "summon", "summoned");
    }

    @Override
    public @Nullable EntityScope find(@NotNull String key) {
        EntityScope p = super.find(key);
        if(p != null) {
            return p;
        }

        // try to find with EntityType
        p = findByEntityType(key);
        if(p != null)
            return p;

        // try again, with removing the 'S' ??
        if(key.endsWith("s")) {
            p = find(key.substring(0, key.length() - 1));
            return p;
        }
        return null;
    }

    private @Nullable EntityScope findByEntityType(@NotNull String key) {
        try {
            EntityType bukkitEntityType = EntityType.valueOf(key.toLowerCase());
            return (e -> e.getType() == bukkitEntityType);
        } catch(IllegalArgumentException ignored) {
            return null;
        }
    }

}
