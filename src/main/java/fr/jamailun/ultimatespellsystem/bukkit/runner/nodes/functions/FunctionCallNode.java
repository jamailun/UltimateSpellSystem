package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.runner.errors.UnknownFunctionException;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellFunction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FunctionCallNode extends RuntimeStatement {

    private final String functionId;
    private final List<RuntimeExpression> arguments;

    public FunctionCallNode(String functionId, List<RuntimeExpression> arguments) {
        this.functionId = functionId;
        this.arguments = arguments;
    }

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        SpellFunction function = UltimateSpellSystem.getSpellsManager().getFunction(functionId);
        if(function == null)
            throw new UnknownFunctionException(functionId);

        List<Object> objects = arguments.stream()
                .map(o -> o.evaluate(runtime))
                .toList();

        function.call(runtime, objects);
    }

}
