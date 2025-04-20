package fr.jamailun.ultimatespellsystem.plugin.spells.external;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.spells.ExternalExecutor;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.plugin.runner.SpellRuntimeImpl;
import fr.jamailun.ultimatespellsystem.plugin.runner.builder.SpellBuilderVisitor;
import fr.jamailun.ultimatespellsystem.plugin.spells.SpellDefinition;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.List;

/**
 * Implementation of a {@link ExternalExecutor}.
 */
public class ExternalExecutorImpl implements ExternalExecutor {

    @Override
    public @NotNull SpellRuntime generateRuntime(@NotNull SpellEntity caster) {
        return new SpellRuntimeImpl(caster);
    }

    @Override
    public @NotNull SpellRuntime generateRuntime(@NotNull LivingEntity caster) {
        return new SpellRuntimeImpl(caster);
    }

    @Override
    public @NotNull @UnmodifiableView List<RuntimeStatement> handleImplementation(@NotNull List<StatementNode> dsl) {
        return Collections.unmodifiableList(SpellDefinition.load(dsl));
    }

    @Override
    public @NotNull RuntimeExpression handleImplementation(@NotNull ExpressionNode dsl) {
        return new SpellBuilderVisitor().convert(dsl);
    }

}
