package fr.jamailun.ultimatespellsystem.bukkit.entities;

import org.bukkit.entity.EntityType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class UssEntityType {

    private final EntityType bukkit;
    private final Class<? extends CustomEntity> custom;

    public UssEntityType(EntityType bukkit) {
        this.bukkit = bukkit;
        this.custom = null;
    }

    public UssEntityType(Class<? extends CustomEntity> custom) {
        this.bukkit = null;
        this.custom = custom;
    }

    public boolean isBukkit() {
        return bukkit != null;
    }

    public EntityType getBukkit() {
        return bukkit;
    }

    public Class<? extends CustomEntity> getCustom() {
        return custom;
    }

    public CustomEntity generateCustom(NewEntityAttributes attributes) {
        assert custom != null;
        try {
            Constructor<? extends CustomEntity> c = custom.getConstructor(NewEntityAttributes.class);
            return c.newInstance(attributes);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Cannot generate custom for " + custom, e);
        }
    }
}
