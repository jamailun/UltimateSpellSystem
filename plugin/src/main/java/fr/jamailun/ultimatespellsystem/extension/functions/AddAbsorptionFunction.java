package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Add absorption hearts.
 */
public class AddAbsorptionFunction extends AbstractFunction {

    public AddAbsorptionFunction() {
        super(
                "add_absorption",
                // Returns new health
                TypePrimitive.BOOLEAN.asType(),
                // Args : the entity to heal, and the amount
                List.of(
                        new FunctionArgument(
                            FunctionType.accept(TypePrimitive.ENTITY),
                            "entity", false
                        ),
                    new FunctionArgument(
                        FunctionType.accept(TypePrimitive.NUMBER),
                        "amount", false
                    ),
                    new FunctionArgument(
                        FunctionType.accept(TypePrimitive.DURATION),
                        "duration", false
                    )
                )
        );
    }

    @Override
    public Boolean compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        LivingEntity target = toLivingEntity("add_absorption:entity", arguments.get(0), runtime);
        double amount = toDouble("add_absorption:amount", arguments.get(1), runtime);
        Duration duration = runtime.safeEvaluate(arguments.get(2), Duration.class);
        if(target == null || duration == null)
            return false;
        var absorptionAttribute = target.getAttribute(Attribute.GENERIC_MAX_ABSORPTION);
        if(absorptionAttribute == null)
            return false;

        var absKey = new NamespacedKey("ultimate-spell-system", "add_absorption-"+ UUID.randomUUID());
        var modifier = new AttributeModifier(absKey, amount, AttributeModifier.Operation.ADD_NUMBER);

        UltimateSpellSystem.getScheduler().run(() -> {
            // Add absorption hearts
            absorptionAttribute.addTransientModifier(modifier);
            target.setAbsorptionAmount(target.getAbsorptionAmount() + amount);
            // Remove after duration
            UltimateSpellSystem.getScheduler().runTaskLater(() -> {
                target.setAbsorptionAmount(Math.max(0, target.getAbsorptionAmount() - amount));
                absorptionAttribute.removeModifier(modifier);
            }, duration.toTicks());
        });

        return true;
    }
}
