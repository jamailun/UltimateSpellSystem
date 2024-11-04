package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions.play;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.utils.holders.ParticleHolder;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class PlayParticleNode extends PlayNode {

    public PlayParticleNode(RuntimeExpression location, RuntimeExpression properties) {
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
