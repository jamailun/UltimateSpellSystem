package fr.jamailun.ultimatespellsystem.bukkit.extensible;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class UssProvider<T> {

    private final Map<String, T> data = new HashMap<>();

    protected String prepare(@NotNull String key) {
        return key.toLowerCase().replace(' ', '_');
    }

    public final void register(@Nonnull T t, String name, String... nameVariants) {
        data.put(prepare(name), t);
        for(String v : nameVariants) {
            data.put(prepare(v), t);
        }
    }

    public @Nullable T find(@NotNull String key) {
        return data.get(prepare(key));
    }

    public final Optional<T> findOptional(String key) {
        if(key == null) return Optional.empty();
        return Optional.ofNullable(find(key));
    }

}
