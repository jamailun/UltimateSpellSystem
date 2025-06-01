package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.play;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.plugin.utils.holders.SoundHolder;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Play sounds in the world.
 */
public class PlaySoundNode extends PlayNode {

    public PlaySoundNode(@NotNull RuntimeExpression location, @NotNull RuntimeExpression properties) {
        super(location, properties);
    }

    @Override
    protected void apply(@NotNull List<Location> locations, @NotNull Map<String, Object> properties) {
        SoundHolder holder = SoundHolder.build("play.sound", properties);
        if(holder != null)
            locations.forEach(holder::apply);
    }

    @Override
    protected boolean isAsync() {
        return false;
    }
}
