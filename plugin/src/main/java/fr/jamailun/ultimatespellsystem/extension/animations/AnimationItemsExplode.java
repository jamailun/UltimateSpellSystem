package fr.jamailun.ultimatespellsystem.extension.animations;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.animations.Animation;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnimationItemsExplode extends Animation {

    public static final String ID = "item-explode";

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

}
