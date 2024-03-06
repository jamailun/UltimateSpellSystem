package fr.jamailun.ultimatespellsystem.bukkit.providers;

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

    protected void postRegister(String key,@NotNull T t) {}

    public final void register(@Nonnull T t, String name, String... nameVariants) {
        String pn = prepare(name);
        data.put(pn, t);
        postRegister(pn, t);

        for(String v : nameVariants) {
            String pv = prepare(v);
            data.put(pv, t);
            postRegister(pv, t);
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
