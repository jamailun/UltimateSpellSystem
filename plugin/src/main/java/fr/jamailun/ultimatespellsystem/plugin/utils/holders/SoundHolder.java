package fr.jamailun.ultimatespellsystem.plugin.utils.holders;

import fr.jamailun.ultimatespellsystem.UssLogger;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Holds a sound effect.
 */
public class SoundHolder {

    private final Sound type;
    private final float volume, pitch;

    /**
     * An empty sound holder.
     */
    public static final SoundHolder NONE = new SoundHolder();

    private SoundHolder() {
        type = null;
        volume = 0;
        pitch = 1;
    }

    public SoundHolder(@NotNull Sound type, float volume, float pitch) {
        this.type = type;
        this.volume = volume;
        this.pitch = pitch;
        UssLogger.logDebug("New sounds-holder : (" + this +")");
    }

    /**
     * Build a Sound-holder from a map.
     * @param context the debug location of the attributes, used for printing-purpose.
     * @param values the map of attributes. Expected keys: {type, volume, pitch}
     * @return null if an error occurred.
     */
    public static @Nullable SoundHolder build(String context, @NotNull Map<?, ?> values) {
        // Type
        Object typeRaw = values.get("type");
        if(!(typeRaw instanceof String type)) {
            UssLogger.logError("(" + context + ") Invalid sound-type : '" + typeRaw + "'.");
            return null;
        }
        Sound sound;
        try {
            sound = Sound.valueOf(type.toUpperCase());
        } catch(IllegalArgumentException e) {
            UssLogger.logError("(" + context + ") Unknown sound-type : '" + type + "' ("+e.getMessage()+")");
            return null;
        }

        // Volume
        float volume = 1;
        if(values.containsKey("volume")) {
            Object raw = values.get("volume");
            if(!(raw instanceof Double ds)) {
                UssLogger.logError("(" + context + ") Invalid sound volume : '" + raw + "'.");
                return null;
            }
            volume = ds.floatValue();
        }

        // Count
        float pitch = 1;
        if(values.containsKey("pitch")) {
            Object raw = values.get("pitch");
            if(!(raw instanceof Double ds)) {
                UssLogger.logError("(" + context + ") Invalid sound pitch : '" + raw + "'.");
                return null;
            }
            pitch = ds.floatValue();
        }

        // Create
        return new SoundHolder(sound, volume, pitch);
    }

    /**
     * Play the sound-effect to a location.
     * @param location the non-ull location to use.
     */
    public void apply(Location location) {
        if(type == null)
            return;
        location.getWorld().playSound(location, type, volume, pitch);
    }

}
