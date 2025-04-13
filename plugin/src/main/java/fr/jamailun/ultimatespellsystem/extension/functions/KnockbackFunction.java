package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.plugin.utils.TypeInterpretation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A simple knockback function.
 */
public class KnockbackFunction extends AbstractFunction {

    public KnockbackFunction() {
        super(
                "knockback",
                // Returns nothing
                TypePrimitive.NULL.asType(),
                // Args :
                // - entity : entity to knockback
                // - vector : velocity to apply.
                // - optional LENGTH
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "entity", false
                        ),
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.LOCATION),
                                "velocity", false
                        ),
                        new FunctionArgument(
                            FunctionType.accept(TypePrimitive.NUMBER),
                            "length", true
                        )
                )
        );
    }

    @Override
    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
      SpellEntity entity = toSpellEntity("knockback.entity", arguments.getFirst(), runtime);
      if(entity == null) {
        UltimateSpellSystem.logError("Null argument for knockback:entity.");
        return null;
      }
      Object vector = arguments.get(1).evaluate(runtime);
      Vector dir = TypeInterpretation.extractDirection(vector);

      if(arguments.size() == 3) {
        Object length = arguments.get(2).evaluate(runtime);
        if(length == null) throw new RuntimeException("Null argument for knockback:length.");
        if(!(length instanceof Number num)) throw new RuntimeException("Length must be a number. It's a " + length.getClass().getSimpleName());
        dir = dir.normalize().multiply(num.doubleValue());
      }

      if(!dir.isZero())
        entity.setVelocity(dir);
      return null;
    }
}
