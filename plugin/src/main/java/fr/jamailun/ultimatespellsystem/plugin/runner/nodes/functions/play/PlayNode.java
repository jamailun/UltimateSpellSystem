package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.play;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Abstract node, for playing something.
 */
@RequiredArgsConstructor
public abstract class PlayNode extends RuntimeStatement {

    private final @NotNull RuntimeExpression location;
    private final @NotNull RuntimeExpression properties;

    protected boolean isAsync() {
        return true;
    }

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        List<Object> list = runtime.safeEvaluateAcceptsList(location, Object.class);
        List<Location> locations = new ArrayList<>();
        for(Object holder : list) {
            if(holder instanceof Location loc) {
                locations.add(loc);
            } else if(holder instanceof SpellEntity entity) {
                locations.add(entity.getLocation());
            } else {
                throw new UnreachableRuntimeException("Invalid type for playNode.locations : " + holder);
            }
        }

        if(locations.isEmpty())
            return;

        // Evaluate variables synchronously.
        // It may cause issue if not.
        Map<String, Object> attributes = getProperties(properties, runtime);

        // Eventually run it async.
        Runnable action = () -> apply(locations, attributes);
        if(isAsync()) {
            UltimateSpellSystem.getScheduler().runAsync(action);
        } else {
            action.run();
        }
    }

    protected abstract void apply(@NotNull List<Location> locations, @NotNull Map<String, Object> properties);

}
