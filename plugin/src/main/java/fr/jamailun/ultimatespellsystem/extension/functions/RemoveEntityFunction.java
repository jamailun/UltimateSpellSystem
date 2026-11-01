package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Heal an entity.
 */
public class RemoveEntityFunction extends AbstractFunction {

    public RemoveEntityFunction() {
        super(
                "remove",
                // Returns true if it worked
                TypePrimitive.BOOLEAN.asType(),
                // Args : the entity to remove
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "entity", false
                        )
                )
        );
    }

    @Override
    public Boolean compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        LivingEntity target = toLivingEntity("remove:entity", arguments.getFirst(), runtime);
        if(target == null || target instanceof Player || !target.isValid())
            return false;

        // remove
        if(UltimateSpellSystem.getSummonsManager().isASummonedEntity(target.getUniqueId())) {
            UltimateSpellSystem.getSummonsManager().remove(target.getUniqueId());
        } else {
            target.remove();
        }

        return true;
    }
}
