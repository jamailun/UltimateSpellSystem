package fr.jamailun.ultimatespellsystem.api.utils;

import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Read items from properties-set in spell.
 */
public interface ItemReader {

    /**
     * Read an item from a data-map.
     * @param map the map to use. {@code Must be String - Any}
     * @param runtime the context to use.
     * @param context the string context to print on errors.
     * @return a non-null item. The material will be {@code AIR} if a problem occurred (or if type not defined).
     */
    @NotNull ItemStack readFromMap(@Nullable Map<?,?> map, @NotNull SpellRuntime runtime, @NotNull String context);

    /**
     * Read an item from a data-map, with some elements already set.
     * @param material the material of the item.
     * @param amount the amount to use.
     * @param data the data-map to use.
     * @param runtime the context to use.
     * @return a non-null item. The material will be {@code AIR} if a problem occurred.
     */
    @NotNull ItemStack readFromMap(@NotNull Material material, int amount, @NotNull Map<String, Object> data, @NotNull SpellRuntime runtime);

}
