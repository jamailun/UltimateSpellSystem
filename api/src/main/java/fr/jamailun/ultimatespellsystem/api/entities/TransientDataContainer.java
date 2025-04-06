package fr.jamailun.ultimatespellsystem.api.entities;

import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class TransientDataContainer implements PersistentDataContainer {

    private final Map<NamespacedKey, Entry<?,?>> primitiveStorage = new HashMap<>();
    private final PersistentDataAdapterContext context = new Context(this);

    @Override
    public <P, C> void set(@NotNull NamespacedKey key, @NotNull PersistentDataType<P, C> type, @NotNull C value) {
        primitiveStorage.put(key, new Entry<>(key, type, type.toPrimitive(value, context)));
    }

    @Override
    public void remove(@NotNull NamespacedKey key) {
        primitiveStorage.remove(key);
    }

    @Override
    public boolean has(@NotNull NamespacedKey key) {
        return primitiveStorage.containsKey(key);
    }

    @Override
    public <P, C> boolean has(@NotNull NamespacedKey key, @NotNull PersistentDataType<P, C> type) {
        Entry<?,?> entry = primitiveStorage.get(key);
        return entry != null && entry.matches(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <P, C> @Nullable C get(@NotNull NamespacedKey key, @NotNull PersistentDataType<P, C> type) {
        Entry<?,?> entry = primitiveStorage.get(key);
        if(entry == null || !entry.matches(type))
            return null;
        return (C) entry.toComplex(context);
    }

    @Override
    public <P, C> @NotNull C getOrDefault(@NotNull NamespacedKey key, @NotNull PersistentDataType<P, C> type, @NotNull C defaultValue) {
        return Objects.requireNonNullElse( get(key, type), defaultValue);
    }

    @Override
    public @NotNull Set<NamespacedKey> getKeys() {
        return primitiveStorage.keySet();
    }

    @Override
    public boolean isEmpty() {
        return primitiveStorage.isEmpty();
    }

    @Override
    public void copyTo(@NotNull PersistentDataContainer other, boolean replace) {
        primitiveStorage.values().forEach(v -> v.copyTo(other, replace, context));
    }

    @Override
    public @NotNull PersistentDataAdapterContext getAdapterContext() {
        return context;
    }

    @Override
    public byte @NotNull [] serializeToBytes() {
        throw new NotImplementedException("Cannot serialize a transient data container.");
    }

    @Override
    public void readFromBytes(byte @NotNull [] bytes, boolean clear) {
        throw new NotImplementedException("Cannot deserialize a transient data container.");
    }

    private record Context(PersistentDataContainer parent) implements PersistentDataAdapterContext {
        @Override
        public @NotNull PersistentDataContainer newPersistentDataContainer() {
            return parent;
        }
    }

    private record Entry<P, V>(@NotNull NamespacedKey key, @NotNull PersistentDataType<P, V> type, @NotNull P primitive) {
        public <Po, Vo> boolean matches(@NotNull PersistentDataType<Po, Vo> other) {
            return Objects.equals(type.getPrimitiveType(), other.getPrimitiveType())
                && Objects.equals(type.getComplexType(), other.getComplexType());
        }
        public @NotNull V toComplex(@NotNull PersistentDataAdapterContext context) {
            return type.fromPrimitive(primitive, context);
        }
        public void copyTo(@NotNull PersistentDataContainer nbt, boolean replace, @NotNull PersistentDataAdapterContext context) {
            if(replace || !nbt.has(key, type)) {
                nbt.set(key, type, toComplex(context));
            }
        }
    }
}
