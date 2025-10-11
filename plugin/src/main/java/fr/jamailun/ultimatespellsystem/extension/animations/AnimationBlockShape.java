package fr.jamailun.ultimatespellsystem.extension.animations;

import fr.jamailun.ultimatespellsystem.api.animations.Animation;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.plugin.utils.Pair;
import fr.jamailun.ultimatespellsystem.plugin.utils.holders.BlockHolder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Multiple block PLAYS, in a shape.
 */
public abstract class AnimationBlockShape extends Animation {

    protected final Duration duration;
    protected final Location location;
    private final List<Material> materials;

    public AnimationBlockShape(Duration duration, @NotNull Location location, @NotNull List<Material> materials) {
        if(materials.isEmpty())
            throw new IllegalArgumentException("Cannot accept an empty list for "+getClass().getSimpleName()+" materials.");
        this.duration = duration;
        this.location = location;
        this.materials = materials;
    }

    @Override
    public long getDuration() {
        return duration.toTicks();
    }

    protected @NotNull Material randomMaterial() {
        return materials.get(random.nextInt(materials.size()));
    }

    protected abstract @NotNull List<Location> buildShape();

    @Override
    protected void onStart() {
        List<Pair<Location, Material>> data = buildShape().stream()
            .map(l -> Pair.of(l, randomMaterial()))
            .toList();
        BlockHolder.applyMany(location.getWorld(), duration, data);
    }

    @Override
    protected void onTick() {
        // Nothing actually
    }

    @Override
    protected void onFinish() {
        // Nothing
    }
}
