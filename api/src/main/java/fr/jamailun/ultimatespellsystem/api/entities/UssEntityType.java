package fr.jamailun.ultimatespellsystem.api.entities;

import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * A way for USS to store entity types.
 */
public class UssEntityType {

    // Bukkit
    @Getter private final EntityType bukkit;

    // Custom
    @Getter private final Class<? extends CustomEntity> custom;
    private final Function<SummonAttributes, ? extends CustomEntity> provider;

    /**
     * If true, the entity expects a projectile-like behaviour. <br/>
     * Moreover, the summon location will use the <b>eyes</b> of the caster.
     */
    @Getter private final boolean projectileLike;

    private UssEntityType(@NotNull EntityType bukkit) {
        this.bukkit = bukkit;
        this.custom = null;
        this.provider = null;
        if(bukkit.getEntityClass() != null) {
            projectileLike = Projectile.class.isAssignableFrom( bukkit.getEntityClass() );
        } else {
            projectileLike = false;
        }
    }

    private UssEntityType(Class<? extends CustomEntity> custom, Function<SummonAttributes, ? extends CustomEntity> provider, boolean projectileLike) {
        this.bukkit = null;
        this.custom = custom;
        this.provider = provider;
        this.projectileLike = projectileLike;
    }

    /**
     * Wrap a Bukkit EntityType into a USS EntityType.
     * @param bukkitType a non-null entity type enum entry.
     * @return a new USS entity type.
     */
    @Contract("_ -> new")
    public static @NotNull UssEntityType ofBukkit(@NotNull EntityType bukkitType) {
        return new UssEntityType(bukkitType);
    }

    /**
     * Register a custom USS entity type, able to generate a {@link CustomEntity} instance.
     * @param clazz the class of the produced instance.
     * @param provider a way to build the entity instance from the {@link SummonAttributes}.
     * @param projectileLike if {@code true}
     * @return a new USS entity type.
     * @param <T> the custom entity type.
     */
    @Contract("_,_,_ -> new")
    public static <T extends CustomEntity> @NotNull UssEntityType ofCustom(
            @NotNull Class<T> clazz,
            @NotNull Function<SummonAttributes, T> provider,
            boolean projectileLike
    ) {
        return new UssEntityType(clazz, provider, projectileLike);
    }

    /**
     * Test if this is a bukkit entity type wrapper.
     * @return {@code false} if the function {@link #generateCustom(SummonAttributes)} can be called.
     */
    public boolean isBukkit() {
        return bukkit != null;
    }

    /**
     * Generate a new custom entity instance.
     * @param attributes the attributes of the summoning.
     * @return a new custom-entity instance.
     * @throws RuntimeException if this USS entity-type is not a custom one.
     * @see #isBukkit()
     */
    public @NotNull CustomEntity generateCustom(@NotNull SummonAttributes attributes) {
        if(custom == null)
            throw new RuntimeException("A USS Entity type of" + this + " should NOT be able to call #generateCustom.");
        return provider.apply(attributes);
    }

    /**
     * Test if a bukkit entity is of this type. Can only work with bukkit entity types.
     * @param entity the entity to test.
     * @return true if this entity type matches the provided entity.
     */
    public boolean isOf(@NotNull Entity entity) {
        if(isBukkit()) {
            return getBukkit() == entity.getType();
        }
        return false;
    }
}
