package fr.jamailun.ultimatespellsystem.extension.functions;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl2.nodes.type.TypePrimitive;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SolidBlockBellowFunction extends AbstractFunction {

    public SolidBlockBellowFunction() {
        super(
                "block_below",
                TypePrimitive.LOCATION.asType(),
                List.of(
                        new FunctionArgument(
                                Type.of(TypePrimitive.LOCATION),
                                "location", false
                        )
                )
        );
    }

    @Override
    public Location compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        Location location = toLocation("block_below:entity", arguments.getFirst(), runtime);

        int worldMinY = location.getWorld().getMinHeight();
        int x = location.getBlockX();
        int z = location.getBlockZ();

        for(int y = location.getBlockY(); y >= worldMinY; y--) {
            Block block = location.getWorld().getBlockAt(x, y, z);
            if( !block.isEmpty() && !block.isPassable()) {
                return block.getLocation();
            }
        }
        return null;
    }
}
