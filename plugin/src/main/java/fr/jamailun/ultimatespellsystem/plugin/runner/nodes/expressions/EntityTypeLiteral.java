package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.api.entities.UssEntityType;
import fr.jamailun.ultimatespellsystem.api.providers.EntityTypeProvider;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.EntityTypeExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Literal for {@link UssEntityType}
 */
public class EntityTypeLiteral extends RuntimeExpression {

    private final String name;

    public EntityTypeLiteral(@NotNull EntityTypeExpression dsl) {
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
