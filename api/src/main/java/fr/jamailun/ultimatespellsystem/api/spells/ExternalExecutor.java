package fr.jamailun.ultimatespellsystem.api.spells;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;

/**
 * This allows for external APIs to evaluate expressions.
 */
public interface ExternalExecutor {

    /**
     * Create a new spell runtime instance.
     * @param caster spell entity to consider as "caster".
     * @return a new spell runtime.
     */
    @Contract("_ -> new")
    @NotNull SpellRuntime generateRuntime(@NotNull SpellEntity caster);

    /**
     * Create a new spell runtime instance.
     * @param caster bukkit entity to consider as "caster".
     * @return a new spell runtime.
     */
    @Contract("_ -> new")
    @NotNull SpellRuntime generateRuntime(@NotNull LivingEntity caster);

    /**
     * Evaluate an expression.
     * @param expression expression to evaluate
     * @param caster the spell-entity caster.
     * @return expression output.
     */
    default @Nullable Object evaluateExpression(@NotNull RuntimeExpression expression, @NotNull SpellEntity caster) {
        return expression.evaluate( generateRuntime(caster) );
    }

    /**
     * Evaluate an expression.
     * @param expression expression to evaluate
     * @param caster the Bukkit caster.
     * @return expression output.
     */
    default @Nullable Object evaluateExpression(@NotNull RuntimeExpression expression, @NotNull LivingEntity caster) {
        return expression.evaluate( generateRuntime(caster) );
    }

    /**
     * Implement DSL into a runnable statement.
     * @param dsl DSL to implement.
     * @return a new unmodifiable list of runnable statements.
     * @throws fr.jamailun.ultimatespellsystem.dsl.errors.UssException in case of issue.
     */
    @NotNull @UnmodifiableView
    List<RuntimeStatement> handleImplementation(@NotNull List<StatementNode> dsl);

    /**
     * Implement DSL into a runnable expression.
     * @param dsl DSL to implement.
     * @return a single DSL expression.
     * @throws fr.jamailun.ultimatespellsystem.dsl.errors.UssException in case of issue.
     */
    @NotNull RuntimeExpression handleImplementation(@NotNull ExpressionNode dsl);

}
