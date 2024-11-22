package fr.jamailun.ultimatespellsystem.api.bukkit.providers;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bukkit.entities.EntityScope;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * Provider for {@link EntityScope} : predicates on {@link org.bukkit.entity.Entity}.
 */
public final class ScopeProvider extends UssProvider<EntityScope> {

    private static final ScopeProvider INSTANCE = new ScopeProvider();
    public static ScopeProvider instance() {
        return INSTANCE;
    }

    // Load custom scopes
    static {
        instance().register(e -> e instanceof LivingEntity, "living", "living_entity", "living_entities", "all", "any");
        instance().register(e -> e instanceof Mob, "mob");
        instance().register(e -> e instanceof Monster, "monster");
        instance().register(e -> e instanceof Animals, "animal");
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
