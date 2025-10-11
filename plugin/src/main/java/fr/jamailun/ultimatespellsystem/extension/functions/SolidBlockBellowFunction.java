package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SolidBlockBellowFunction extends AbstractFunction {

    public SolidBlockBellowFunction() {
        super(
                "block_below",
                TypePrimitive.LOCATION.asType(),
                List.of(
                        new FunctionArgument(
                                FunctionType.accept(TypePrimitive.ENTITY),
                                "entity", false
                        )
                )
        );
    }

    @Override
    public Location compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        LivingEntity entity = toLivingEntity("block_below:entity", arguments.getFirst(), runtime);
        if(entity == null) return null;
        int worldMinY = entity.getWorld().getMinHeight();
        int x = entity.getLocation().getBlockX();
        int z = entity.getLocation().getBlockZ();

        for(int y = entity.getLocation().getBlockY(); y >= worldMinY; y--) {
            Block block = entity.getWorld().getBlockAt(x, y, z);
            if( !block.isEmpty() && !block.isPassable()) {
                return block.getLocation();
            }
        }
        return null;
    }
}
