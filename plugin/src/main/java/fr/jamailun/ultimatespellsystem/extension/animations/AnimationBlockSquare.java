package fr.jamailun.ultimatespellsystem.extension.animations;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.providers.AnimationsProvider;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Animation with items "exploding" and then staying on the ground for a small duration.
 */
public class AnimationBlockSquare extends AnimationBlockShape {

    public static final String ID = "blocks.square";

    private final int radius;

    public AnimationBlockSquare(Duration duration, @NotNull Location location, @NotNull List<Material> materials, double radius) {
        super(duration, location, materials);
        this.radius = (int) Math.round(radius);
    }

    @Override
    protected @NotNull List<Location> buildShape() {
        List<Location> locations = new ArrayList<>();
        for(int dx = -radius; dx <= radius; dx++) {
            for(int dz = -radius; dz <= radius; dz++) {
                locations.add(location.clone().add(dx, 0, dz));
            }
        }
        return locations;
    }

    /**
     * Generate an animation generator.
     * @return a new generator.
     */
    public static @NotNull AnimationsProvider.AnimationGenerator generator() {
        return (location, data) -> {
            try {
                Duration duration = Helper.as(data, Duration.class, "duration", ID);
                double radius = Helper.as(data, Number.class, "radius", ID).doubleValue();
                List<Material> materials = Helper.listMaterials(data, "types", ID);
                return new AnimationBlockSquare(duration, location, materials, radius);
            } catch (Helper.MissingProperty | Helper.BadProperty e) {
                UssLogger.logError(e.getMessage());
            } catch(IllegalArgumentException e) {
                UssLogger.logError("Animation: unknown Material. " + e.getMessage());
            }
            return null;
        };
    }

}
