package fr.jamailun.ultimatespellsystem.plugin.spells;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.plugin.runner.SpellRuntimeImpl;
import fr.jamailun.ultimatespellsystem.dsl.UltimateSpellSystemDSL;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.validators.DslValidator;
import fr.jamailun.ultimatespellsystem.plugin.runner.builder.SpellBuilderVisitor;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * A spell definition is the common implementation for a {@link fr.jamailun.ultimatespellsystem.api.bukkit.spells.Spell}.
 */
public class SpellDefinition extends AbstractSpell {

    private final List<RuntimeStatement> steps;

    /**
     * Create a new spell definition.
     * @param name the name of the spell.
     * @param steps the steps to run.
     */
    public SpellDefinition(@NotNull String name, @NotNull List<RuntimeStatement> steps) {
        super(name);
        this.steps = steps;
    }

    /**
     * Load a {@link SpellDefinition} from a file.
     * @param file the file to load.
     * @return a new spell definition.
     */
    public static @Nullable SpellDefinition loadFile(@NotNull File file) {
        String name = file.getName()
                .replace(" ", "-")
                .toLowerCase()
                .replaceFirst("[.][^.]+$", "");

        if(name.startsWith(".")) {
            UltimateSpellSystem.logInfo("Skip " + file.getName());
            return null;
        }

        UltimateSpellSystem.logDebug("Extracted '"+name+"' from name '" + file.getName()+"'.");
        return loadFile(name, file);
    }

    /**
     * Load a {@link SpellDefinition} from a file, but with a specific name.
     * @param name the name to use.
     * @param file the file to load.
     * @return a new spell definition.
     */
    public static @Nullable SpellDefinition loadFile(@NotNull String name, @NotNull File file) {
        try {
            List<StatementNode> dsl = UltimateSpellSystemDSL.parse(file);
            DslValidator.validateDsl(dsl);
            List<RuntimeStatement> steps = SpellBuilderVisitor.build(dsl);
            return new SpellDefinition(name, steps);
        } catch(Exception e) {
            UltimateSpellSystem.logError("In "+file+" : " + e.getMessage());
            for(StackTraceElement se : e.getStackTrace()) {
                UltimateSpellSystem.logDebug("  §c" + se.toString());
            }
            return null;
        }
    }

    @Override
    protected boolean castSpell(@NotNull LivingEntity caster) {
        String prefix = "SpellRun-" + UUID.randomUUID().toString().substring(20) + " | ";

        UltimateSpellSystem.logDebug(prefix + " Casted on " + caster);

        SpellRuntime runtime = new SpellRuntimeImpl(caster);
        for(RuntimeStatement statement : steps) {
            UltimateSpellSystem.logDebug(prefix + "Running " + statement.toString());
            statement.run(runtime);

            if(runtime.isStopped())
                break;
        }

        boolean success = runtime.getFinalExitCode() == 0;
        UltimateSpellSystem.logDebug(prefix + "End of cast on " + caster + " with code " + runtime.getFinalExitCode() + ". Success = " + success);
        return success;
    }
}
