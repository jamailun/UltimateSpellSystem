package fr.jamailun.ultimatespellsystem.extension.animations;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.animations.Animation;
import fr.jamailun.ultimatespellsystem.api.providers.AnimationsProvider;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Animation with items "exploding" and then staying on the ground for a small duration.
 */
public class AnimationItemsExplode extends Animation {

    public static final String ID = "explode.items";

    @Getter private final long duration;
    private final Location location;
    private final List<Material> itemTypes;
    private final int count;

    private final Set<Item> items = new HashSet<>();

    public AnimationItemsExplode(long duration, @NotNull Location location, @NotNull List<Material> itemTypes, int count) {
        if(itemTypes.isEmpty())
            throw new IllegalArgumentException("Cannot accept an empty list for AnimationItemsExplode materials.");
        this.duration = duration;
        this.location = location;
        this.itemTypes = itemTypes;
        this.count = count;
    }

    private Material randomMaterial() {
        return itemTypes.get(random.nextInt(itemTypes.size()));
    }

    @Override
    protected void onStart() {
        UltimateSpellSystem.getScheduler().run(() -> {
            World world = location.getWorld();
            for(int i = 0; i < count; i++) {
                ItemStack is = new ItemStack(randomMaterial());
                Item item = world.dropItem(location, is, it -> {
                    it.setCanMobPickup(false);
                    it.setCanPlayerPickup(false);
                    it.setWillAge(false);
                    it.setUnlimitedLifetime(true);
                });
                items.add(item);
            }
        });
    }

    @Override
    protected void onTick() {
        // Nothing actually
    }

    @Override
    protected void onFinish() {
        UltimateSpellSystem.getScheduler().run(() -> {
            items.forEach(Entity::remove);
        });
    }

    /**
     * Generate an animation generator.
     * @return a new generator.
     */
    public static @NotNull AnimationsProvider.AnimationGenerator generator() {
        return (location, data) -> {
            try {
                Duration duration = Helper.as(data, Duration.class, "duration", ID);
                int count = Helper.asOpt(data, Number.class, "count", ID, 5).intValue();
                Object rawTypes = Helper.as(data, Object.class, "types", ID);
                List<Material> materials = Helper.listOfEnumAcceptsMono(Material.class, rawTypes);
                if(materials == null) {
                    UssLogger.logError("Animation: unrecognized animation 'types' : " + rawTypes + " (" + rawTypes.getClass().getSimpleName() + ")");
                    return null;
                }
                return new AnimationItemsExplode(duration.toTicks(), location, materials, count);
            } catch (Helper.MissingProperty | Helper.BadProperty e) {
                UssLogger.logError(e.getMessage());
            } catch(IllegalArgumentException e) {
                UssLogger.logError("Animation: unknown Material. " + e.getMessage());
            }
            return null;
        };
    }

}
