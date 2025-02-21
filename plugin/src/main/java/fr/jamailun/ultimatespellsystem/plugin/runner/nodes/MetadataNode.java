package fr.jamailun.ultimatespellsystem.plugin.runner.nodes;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.spells.SpellMetadata;
import fr.jamailun.ultimatespellsystem.plugin.runner.MetaRuntime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Node for {@link SpellMetadata}.
 */
@RequiredArgsConstructor
@Getter
public class MetadataNode extends RuntimeStatement implements SpellMetadata {

    private final String name;
    private final List<RuntimeExpression> params;

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
    public <T> T get(int index, Class<T> clazz) {
        if(index < 0 || params.size() <= index) return null;
        RuntimeExpression param = params.get(index);
        return MetaRuntime.getInstance().safeEvaluate(param, clazz);
    }

    @Override
    public <T> List<T> get(Class<T> clazz) {
        return params.stream()
                .map(e -> MetaRuntime.getInstance().safeEvaluate(e, clazz))
                .toList();
    }

}
