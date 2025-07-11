package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.play;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.plugin.utils.holders.ParticleHolder;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Play particles in the world.
 */
public class PlayParticleNode extends PlayNode {

    public PlayParticleNode(@NotNull RuntimeExpression location, @NotNull RuntimeExpression properties) {
        super(location, properties);
    }

    @Override
    protected void apply(@NotNull List<Location> locations, @NotNull Map<String, Object> properties) {
        double radius = (double) properties.getOrDefault("radius", 0.4d);
        ParticleHolder holder = ParticleHolder.build("play.particle", radius, properties);
        if(holder != null)
            locations.forEach(holder::apply);
    }

}
