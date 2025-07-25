package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.play;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.plugin.utils.holders.BlockHolder;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Play a "block effect" : make a block appear client side, and then cancel it.
 */
public class PlayBlockNode extends PlayNode {

    public PlayBlockNode(@NotNull RuntimeExpression location, @NotNull RuntimeExpression properties) {
        super(location, properties);
    }

    @Override
    protected void apply(@NotNull List<Location> locations, @NotNull Map<String, Object> properties) {
        BlockHolder holder = BlockHolder.build("play.block", properties);
        if(holder != null)
            locations.forEach(holder::apply);
    }

}
