package fr.jamailun.ultimatespellsystem.plugin.runner;

import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Used by {@link fr.jamailun.ultimatespellsystem.plugin.runner.nodes.MetadataNode}.
 */
public final class MetaRuntime extends AbstractSpellRuntime {

    private static MetaRuntime instance;
    public static MetaRuntime getInstance() {
        if(instance == null)
            instance = new MetaRuntime();
        return instance;
    }

    @Override
    public @NotNull LivingEntity getCaster() {
        throw new RuntimeException("Cannot reference the caster from the metadata.");
    }

    @Override
    public @NotNull SpellRuntime makeChild() {
        return this;
    }
}
