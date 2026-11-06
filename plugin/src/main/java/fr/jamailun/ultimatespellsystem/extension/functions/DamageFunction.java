package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.events.EntityDamagedBySpellEvent;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Damage an entity
 */
public class DamageFunction extends AbstractFunction {

    public DamageFunction() {
        super(
                "damage_entity",
                // Returns true if success
                TypePrimitive.BOOLEAN.asType(),
                // Args : the location to strike
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "target", false
                        ),
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.NUMBER),
                                "amount", false
                        ),
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "author", true
                        )
                )
        );
    }

    @Override
    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        LivingEntity target = toLivingEntity("damage:target", arguments.getFirst(), runtime);
        if(target == null)
            return false;

        double amount = toDouble("damage:amount", arguments.get(1), runtime);

        // author is specified ?
        if(arguments.size() > 2) {
            LivingEntity author = toLivingEntity("damage:author", arguments.get(2), runtime);
            if(author != null) {
                double finalDamage = computeDamage(target, runtime, amount, author);
                if(finalDamage > 0) {
                    target.damage(finalDamage, author);
                    return true;
                }
            }
        }

        // Damage with event
        double finalDamage = computeDamage(target, runtime, amount, null);
        if(finalDamage > 0) {
            target.damage(finalDamage);
        }
        return true;
    }

    private static double computeDamage(LivingEntity target, @NotNull SpellRuntime runtime, double amount, @Nullable LivingEntity cause) {
        var event = new EntityDamagedBySpellEvent(target, runtime.getCaster(), runtime.getSpell(), amount, cause);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled())
            return 0;
        return event.getAmount();
    }
}
