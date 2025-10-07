package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.events.EntityHealedBySpellEvent;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Heal an entity.
 */
public class HealEntityFunction extends AbstractFunction {

    public HealEntityFunction() {
        super(
                "heal",
                // Returns new health
                TypePrimitive.NUMBER.asType(),
                // Args : the entity to heal, and the amount
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "entity", false
                        ),
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.NUMBER),
                                "amount", false
                        )
                )
        );
    }

    @Override
    public Double compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        LivingEntity target = toLivingEntity("heal:entity", arguments.get(0), runtime);
        double amount = toDouble("heal:amount", arguments.get(1), runtime);
        if(target == null)
            return 0d;

        // Heal with an event
        var event = new EntityHealedBySpellEvent(target, runtime.getCaster(), runtime.getSpell(), amount);
        Bukkit.getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
            target.heal(amount);
        }

        return target.getHealth();
    }
}
