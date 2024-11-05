package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.operators;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.runner.errors.OutOfBoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class ArrayGetNode extends RuntimeExpression {

    private final RuntimeExpression array;
    private final RuntimeExpression index;

    @Override
    public @NotNull Object evaluate(@NotNull SpellRuntime runtime) {
        int index = runtime.safeEvaluate(this.index, Double.class).intValue();
        if(index < 0)
            throw new OutOfBoundException(index);

        List<Object> list = runtime.safeEvaluateList(this.array, Object.class);
        if(list.size() <= index)
            throw new OutOfBoundException(this.array, index, list.size());

        return list.get(index);
    }
}
