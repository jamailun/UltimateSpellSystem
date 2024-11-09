package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.runner.errors.UnknownFunctionException;
import fr.jamailun.ultimatespellsystem.bukkit.runner.functions.RunnableJavaFunction;
import fr.jamailun.ultimatespellsystem.bukkit.spells.Spell;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A ray-cast to find a hit block.
 */
public class CastSpellFunction extends RunnableJavaFunction {

    public CastSpellFunction() {
        super(
                "cast",
                // Returns a boolean
                TypePrimitive.BOOLEAN.asType(),
                // Args :
                // - caster : the caster
                // - spell : the spell ID
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "caster", false
                        ),
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.STRING),
                                "spell", false
                        )
                )
        );
    }

    @Override
    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        SpellEntity entity = runtime.safeEvaluate(arguments.get(0), SpellEntity.class);
        String spellId = runtime.safeEvaluate(arguments.get(1), String.class);

        Spell spell = UltimateSpellSystem.getSpellsManager().getSpell(spellId);
        if(spell == null) {
            throw new UnknownFunctionException("Unknown spell ID: '" + spellId + "'.");
        }

        Entity bukkitEntity = entity.getBukkitEntity().orElse(null);
        if(bukkitEntity instanceof LivingEntity living) {
            return spell.castNotCancellable(living);
        }
        return false;
    }
}
