package fr.jamailun.examples.citizens;

import fr.jamailun.examples.utils.DurationHelper;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.dsl.UltimateSpellSystemDSL;
import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import net.citizensnpcs.api.util.DataKey;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Serializable cast rule : CD + condition.
 */
public class SpellCastRule {

    private final String spellId;
    private final String rawCooldown;
    private final String rawCondition;

    private transient Instant nextExecute = Instant.now();
    private transient Duration cooldown;
    private transient RuntimeExpression condition;

    public SpellCastRule(@NotNull String spellId, @NotNull String rawCooldown, @NotNull String rawCondition) {
        this.spellId = spellId;
        this.rawCooldown = rawCooldown;
        this.rawCondition = rawCondition;
        load();
    }

    private void load() {
        cooldown = DurationHelper.parse(rawCooldown, Duration.of(10, ChronoUnit.SECONDS));
        // parse conditions
        if(rawCondition.isEmpty()) {
            condition = null;
        } else {
            ExpressionNode expression = UltimateSpellSystemDSL.parseExpression(rawCondition);
            condition = UltimateSpellSystem.getExternalExecutor().handleImplementation(expression);
        }
    }

    private boolean testCondition(@NotNull SpellRuntime runtime) {
        if(condition == null) return true;

        return switch (condition.evaluate(runtime)) {
            case null -> false;
            case Double d -> d != 0;
            case Boolean b -> b;
            default -> true;
        };
    }

    @Override
    public String toString() {
        return "[" + spellId + ", cd="+cooldown
                + (rawCondition == null || rawCondition.isEmpty() ? "" : "; " + rawCondition)
                + "]";
    }

    private SpellRuntime runtime;
    public boolean canExecute(@NotNull SpellEntity entity) {
        Instant now = Instant.now();
        if(runtime == null)
            runtime = UltimateSpellSystem.getExternalExecutor().generateRuntime(entity);
        if(now.isAfter(nextExecute) && testCondition(runtime)) {
            nextExecute = now.plus(cooldown);
            runtime = null;
            return true;
        }
        return false;
    }

    public String getSpellId() {
        return spellId;
    }

    public static @NotNull SpellCastRule create(@NotNull DataKey key) {
        String spellId = key.getString("spell-id");
        String cooldown = key.getString("cooldown");
        String condition = key.getString("condition");
        return new SpellCastRule(spellId, cooldown, condition);
    }

    public void save(@NotNull DataKey key) {
        key.setString("spell-id", getSpellId());
        key.setString("cooldown", rawCooldown);
        key.setString("condition", rawCondition);
    }
}
