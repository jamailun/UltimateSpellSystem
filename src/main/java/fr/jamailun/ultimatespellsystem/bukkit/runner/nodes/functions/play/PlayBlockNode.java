package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions.play;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.utils.holders.BlockHolder;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

public class PlayBlockNode extends PlayNode {

    public PlayBlockNode(RuntimeExpression location, RuntimeExpression properties) {
        super(location, properties);
    }

    @Override
    protected void apply(List<Location> locations, Map<String, Object> properties) {
        BlockHolder holder = BlockHolder.build("play.block", properties);
        if(holder != null)
            locations.forEach(holder::apply);
    }

}
