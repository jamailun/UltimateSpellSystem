package fr.jamailun.ultimatespellsystem.plugin.runner.nodes;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.spells.SpellMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Node for {@link SpellMetadata}.
 */
@RequiredArgsConstructor
@Getter
public class MetadataNode extends RuntimeStatement implements SpellMetadata {

    private final String name;
    private final List<Object> params;

    @Override
    public void run(@NotNull SpellRuntime runtime) {}

    @Override
    public boolean isEmpty() {
        return params.isEmpty();
    }

    @Override
    public int size() {
        return params.size();
    }

    @Override
    public <T> T get(int index, @NotNull Class<T> clazz) {
        return clazz.cast(getRaw(index));
    }

    @Override
    public <T> @NotNull List<T> get(@NotNull Class<T> clazz) {
        return params.stream()
                .map(clazz::cast)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public Object getRaw(int index) {
        if(index < 0 || params.size() <= index) return null;
        return params.get(index);
    }
}
