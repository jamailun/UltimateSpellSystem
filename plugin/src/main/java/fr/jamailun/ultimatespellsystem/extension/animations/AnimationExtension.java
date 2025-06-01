package fr.jamailun.ultimatespellsystem.extension.animations;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.providers.AnimationsProvider;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class AnimationExtension {
  private AnimationExtension() {}

  public void register() {
    AnimationsProvider.instance().register((location, data) -> {
      try {
        Duration duration = as(data, Duration.class, "duration", AnimationItemsExplode.ID);
        int count = as(data, Number.class, "count", AnimationItemsExplode.ID).intValue();
        Object rawTypes = as(data, Object.class, "types", AnimationItemsExplode.ID);
        List<Material> materials;
        if(rawTypes instanceof String typeStr) {
          Material material = Material.valueOf(typeStr.toUpperCase());
          materials = Collections.singletonList(material);
        } else if(rawTypes instanceof Collection<?> typesList) {
          materials = new ArrayList<>(typesList.size());
          for(Object type : typesList) {
            materials.add(Material.valueOf(type.toString().toUpperCase()));
          }
        } else {
          UssLogger.logError("Animation: unrecognized animation 'types' : " + rawTypes + " (" + rawTypes.getClass().getSimpleName() + ")");
          return null;
        }
        return new AnimationItemsExplode(duration.toTicks(), location, materials, count);
      } catch (MissingProperty | BadProperty e) {
        UssLogger.logError(e.getMessage());
      } catch(IllegalArgumentException e) {
        UssLogger.logError("Animation: unknown Material. " + e.getMessage());
      }
      return null;
    }, "item-explode");
  }

  private <T> @NotNull T as(Map<String, Object> data, Class<T> clazz, String key, String animation) throws MissingProperty, BadProperty {
    Object raw = data.get(key);
    if(raw == null)
      throw new MissingProperty(animation, key);
    try {
      return clazz.cast(raw);
    } catch (ClassCastException e) {
      throw new BadProperty(animation, key, clazz, raw);
    }
  }

  private static class MissingProperty extends Exception {
    public MissingProperty(String animation, String key) {
      super("Animation " + animation + " is missing key '" + key + "'.");
    }
  }
  private static class BadProperty extends Exception {
    public BadProperty(String animation, String key, Class<?> clazz, Object value) {
      super("Animation " + animation + "::" + key + "' bad property type. Expected " +  clazz.getSimpleName() + ", got " + value.getClass().getSimpleName());
    }
  }

}
