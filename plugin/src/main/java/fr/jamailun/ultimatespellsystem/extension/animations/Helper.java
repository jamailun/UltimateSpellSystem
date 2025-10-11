package fr.jamailun.ultimatespellsystem.extension.animations;

import fr.jamailun.ultimatespellsystem.UssLogger;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Package-protected helper class to register {@link fr.jamailun.ultimatespellsystem.api.animations.Animation}.
 */
final class Helper {
  private Helper() {/* empty */}

  static <T> @NotNull T as(Map<String, Object> data, Class<T> clazz, String key, String animation) throws MissingProperty, BadProperty {
    Object raw = data.get(key);
    if(raw == null)
      throw new MissingProperty(animation, key);
    try {
      return clazz.cast(raw);
    } catch (ClassCastException e) {
      throw new BadProperty(animation, key, clazz, raw);
    }
  }

  static <T extends Enum<T>> @NotNull T asEnum(Map<String, Object> data, Class<T> clazz, String key, String animation) throws MissingProperty, BadProperty {
    Object raw = data.get(key);
    if(raw == null)
      throw new MissingProperty(animation, key);
    try {
      return Enum.valueOf(clazz, String.valueOf(raw).toUpperCase());
    } catch (ClassCastException e) {
      throw new BadProperty(animation, key, clazz, raw);
    }
  }

  static <T> T asOpt(Map<String, Object> data, Class<T> clazz, String key, String animation, @Nullable T defaultValue) throws BadProperty {
    try {
        return as(data, clazz, key, animation);
    } catch (MissingProperty e) {
      return defaultValue;
    }
  }

  static <T extends Enum<T>> @Nullable List<T> listOfEnumAcceptsMono(Class<T> clazz, Object rawValue) {
    if(rawValue instanceof String str) {
      T t = Enum.valueOf(clazz, str.toUpperCase());
      return Collections.singletonList(t);
    }

    if(rawValue instanceof Collection<?> typesList) {
      List<T> list = new ArrayList<>(typesList.size());
      for(Object type : typesList) {
        list.add(Enum.valueOf(clazz, type.toString().toUpperCase()));
      }
      return list;
    }

    return null;
  }

  static @NotNull List<Material> listMaterials(Map<String, Object> data, String key, String animation) throws MissingProperty, BadProperty {
    Object rawTypes = as(data, Object.class, key, animation);
    List<Material> materials = Helper.listOfEnumAcceptsMono(Material.class, rawTypes);
    if(materials == null) {
      UssLogger.logError("Animation "+animation+": unrecognized animation '"+key+"' : " + rawTypes + " (" + rawTypes.getClass().getSimpleName() + ")");
      throw new BadProperty(animation, key);
    }
    return materials;
  }

  static <T extends Enum<T>> @Nullable T enumMono(Class<T> clazz, Object rawValue) {
    if(rawValue instanceof String str) {
      return Enum.valueOf(clazz, str.toUpperCase());
    }
    return null;
  }

  static class MissingProperty extends Exception {
    public MissingProperty(String animation, String key) {
      super("Animation " + animation + " is missing key '" + key + "'.");
    }
  }

  static class BadProperty extends Exception {
    public BadProperty(String animation, String key, Class<?> clazz, Object value) {
      super("Animation " + animation + "::" + key + "' bad property type. Expected " +  clazz.getSimpleName() + ", got " + value.getClass().getSimpleName());
    }
    public BadProperty(String animation, String key) {
      super("Animation " + animation + "::" + key + "' bad property value. Cannot accept an empty list.");
    }
  }

}
