package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.api.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.errors.UnknownWorldException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class LocationNode extends RuntimeExpression {

    private final RuntimeExpression world, x, y, z;
    private final RuntimeExpression yaw, pitch; // Nullable

    public LocationNode(RuntimeExpression world, RuntimeExpression x, RuntimeExpression y, RuntimeExpression z, RuntimeExpression yaw, RuntimeExpression pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public Location evaluate(@NotNull SpellRuntime runtime) {
        String worldName = runtime.safeEvaluate(this.world, String.class);
        World world = Bukkit.getWorld(worldName);
        if(world == null)
            throw new UnknownWorldException(worldName);

        double x = runtime.safeEvaluate(this.x, Double.class);
        double y = runtime.safeEvaluate(this.y, Double.class);
        double z = runtime.safeEvaluate(this.z, Double.class);
        if(yaw != null && pitch != null) {
            float yaw = runtime.safeEvaluate(this.yaw, Double.class).floatValue();
            float pitch = runtime.safeEvaluate(this.pitch, Double.class).floatValue();
            return new Location(world, x, y, z, yaw, pitch);
        }
        return new Location(world, x, y, z);
    }

    @Override
    public String toString() {
        return "LOC("+world+", " + x + ", " + y + ", " + z + ")";
    }
}
