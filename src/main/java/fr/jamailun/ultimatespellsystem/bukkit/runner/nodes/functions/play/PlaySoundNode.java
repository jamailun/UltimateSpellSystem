package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions.play;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.utils.holders.SoundHolder;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

public class PlaySoundNode extends PlayNode {

    public PlaySoundNode(RuntimeExpression location, RuntimeExpression properties) {
        super(location, properties);
    }

    @Override
    protected void apply(List<Location> locations, Map<String, Object> properties) {
        SoundHolder holder = SoundHolder.build("play.sound", properties);
        if(holder != null)
            locations.forEach(holder::apply);
    }

}
