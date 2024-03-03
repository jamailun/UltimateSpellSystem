package fr.jamailun.ultimatespellsystem.bukkit.extensible;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public final class ScopeProvider extends UssProvider<Predicate<Entity>> {

    private static final ScopeProvider INSTANCE = new ScopeProvider();
    public static ScopeProvider instance() {
        return INSTANCE;
    }

    static {
        // Custom scopes
        instance().register(e -> e instanceof Mob, "MOB", "MOBS");
        instance().register(e -> e instanceof Monster, "MONSTER", "MONSTERS");
        instance().register(e -> e instanceof Animals, "ANIMAL", "ANIMALS");
        instance().register(e -> UltimateSpellSystem.getSummonsManager().isASummonedEntity(e.getUniqueId()), "SUMMON", "SUMMONS", "SUMMONED");
    }

    public @Nullable Predicate<Entity> find(@NotNull String key) {
        Predicate<Entity> p = super.find(key);
        if(p != null)
            return p;

        // try to find with EntityType
        p = findByEntityType(key);
        if(p != null)
            return p;

        // try again, with removing the 'S' ??
        if(key.endsWith("s")) {
            p = findByEntityType(key.substring(0, key.length() - 1));
            return p;
        }
        return null;
    }

    private @Nullable Predicate<Entity> findByEntityType(@NotNull String key) {
        try {
            EntityType bukkitEntityType = EntityType.valueOf(key.toUpperCase());
            return (e -> e.getType() == bukkitEntityType);
        } catch(IllegalArgumentException ignored) {
            return null;
        }
    }
}
