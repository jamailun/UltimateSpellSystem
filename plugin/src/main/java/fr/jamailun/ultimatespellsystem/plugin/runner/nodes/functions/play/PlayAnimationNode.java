package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.play;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.animations.Animation;
import fr.jamailun.ultimatespellsystem.api.providers.AnimationsProvider;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * To play an {@link fr.jamailun.ultimatespellsystem.api.animations.Animation}.
 */
public class PlayAnimationNode extends PlayNode {

    public PlayAnimationNode(@NotNull RuntimeExpression location, @NotNull RuntimeExpression properties) {
        super(location, properties);
    }

    @Override
    protected void apply(@NotNull List<Location> locations, @NotNull Map<String, Object> properties) {
        Object rawId = properties.get("id");
        if(!(rawId instanceof String id)) {
            UssLogger.logError("Missing animation ID ! Add the 'id' field in the PLAY ANIMATION statement.");
            return;
        }
        if(!AnimationsProvider.instance().exists(id)) {
            UssLogger.logError("Unknown animation ID: '" + id + "'.");
            return;
        }
        for(Location location : locations) {
            Animation animation = AnimationsProvider.instance().generate(id, location, properties);
            if(animation != null) {
                UltimateSpellSystem.getAnimationsManager().play(animation);
            }
        }
    }
}
