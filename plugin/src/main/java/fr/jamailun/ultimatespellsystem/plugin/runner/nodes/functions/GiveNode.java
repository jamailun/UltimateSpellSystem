package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.api.providers.ItemReader;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.errors.InvalidTypeException;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@RequiredArgsConstructor
public class GiveNode extends RuntimeStatement {

    private final @NotNull RuntimeExpression target;
    private final @Nullable RuntimeExpression optAmount;
    private final @Nullable RuntimeExpression optType;
    private final @Nullable RuntimeExpression optProperties;

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        // Extract inventory-holder
        SpellEntity entity = runtime.safeEvaluate(target, SpellEntity.class);
        if(entity.getBukkitEntity().isEmpty()) return;
        Entity bukkitEntity = entity.getBukkitEntity().orElse(null);
        if(!(bukkitEntity instanceof InventoryHolder holder)) return;

        // Properties
        Map<String, Object> properties = getProperties(optProperties, runtime);

        // Amount
        int amount = optAmount == null ? 1 : runtime.safeEvaluate(optAmount, Double.class).intValue();
        if(properties.containsKey("amount"))
            amount = (int) properties.get("amount");

        // Material
        Material material = handleMaterial(optType == null ? Material.AIR : optType.evaluate(runtime));
        if(properties.containsKey("type"))
            material = handleMaterial(properties.get("type"));

        // Add the item
        ItemStack item = ItemReader.instance().readFromMap(material, amount, properties, runtime);
        holder.getInventory().addItem(item);
    }

    private Material handleMaterial(Object object) {
        if(object instanceof Material mat) {
            return mat;
        } else if(object instanceof String str) {
            return Material.valueOf(str.toUpperCase());
        }
        throw new InvalidTypeException("give:material", "Material/String", object);
    }
}
