package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions.play;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.runner.errors.UnreachableRuntimeException;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class PlayNode extends RuntimeStatement {

    private final RuntimeExpression location;
    private final RuntimeExpression properties;

    public PlayNode(RuntimeExpression location, RuntimeExpression properties) {
        this.location = location;
        this.properties = properties;
    }

    @Override
    public void run(SpellRuntime runtime) {
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

        Map<String, Object> attributes = getProperties(properties, runtime);

        apply(locations, attributes);
    }

    protected abstract void apply(List<Location> locations, Map<String, Object> properties);

}
