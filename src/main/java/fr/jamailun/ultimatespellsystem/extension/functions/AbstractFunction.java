package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.bukkit.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.runner.errors.InvalidTypeException;
import fr.jamailun.ultimatespellsystem.bukkit.runner.functions.RunnableJavaFunction;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractFunction extends RunnableJavaFunction {

    protected AbstractFunction(@NotNull String id, @NotNull Type type, @NotNull List<FunctionArgument> arguments) {
        super(id, type, arguments);
    }

    protected @NotNull Location toLocation(@NotNull String context, @NotNull RuntimeExpression expression, @NotNull SpellRuntime runtime) {
        Object locationRaw = expression.evaluate(runtime);
        if(locationRaw instanceof Location loc) {
            return loc;
        } else if(locationRaw instanceof SpellEntity entity) {
            return entity.getEyeLocation();
        } else {
            throw new InvalidTypeException(context, "location or entity", locationRaw);
        }
    }

}
