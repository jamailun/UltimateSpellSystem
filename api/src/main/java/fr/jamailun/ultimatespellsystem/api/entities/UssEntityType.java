package fr.jamailun.ultimatespellsystem.api.entities;

import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Wraps Entity Types to allow custom entities.
 */
public class UssEntityType {

    @Getter private final EntityType bukkit;
    @Getter private final Class<? extends CustomEntity> custom;
    private final boolean preferEyesLocation; // si oui, alors la Location de summon sera les YEUX du caster

    public UssEntityType(EntityType bukkit) {
        this.bukkit = bukkit;
        this.custom = null;
        preferEyesLocation = false;
    }

    public UssEntityType(Class<? extends CustomEntity> custom) {
        this.bukkit = null;
        this.custom = custom;
        preferEyesLocation = true;
    }

    public boolean doesPreferEyesLocation() {
        return preferEyesLocation;
    }

    public boolean isBukkit() {
        return bukkit != null;
    }

    public CustomEntity generateCustom(SummonAttributes attributes) {
        assert custom != null;
        try {
            Constructor<? extends CustomEntity> c = custom.getConstructor(SummonAttributes.class);
            return c.newInstance(attributes);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Cannot generate custom for " + custom, e);
        }
    }

    public boolean isOf(Entity entity) {
        if(isBukkit()) {
            return getBukkit() == entity.getType();
        }
        return false;
    }
}
