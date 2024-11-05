package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.bukkit.entities.UssEntityType;
import fr.jamailun.ultimatespellsystem.bukkit.providers.EntityTypeProvider;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.EntityTypeExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Literal for {@link UssEntityType}
 */
public class EntityTypeLiteral extends RuntimeExpression {

    private final String name;

    public EntityTypeLiteral(EntityTypeExpression dsl) {
        this.name = dsl.getRaw();
    }

    @Override
    public UssEntityType evaluate(@NotNull SpellRuntime runtime) {
        return EntityTypeProvider.instance().find(name);
    }

    @Override
    public String toString() {
        return "EntityType." + name;
    }
}
