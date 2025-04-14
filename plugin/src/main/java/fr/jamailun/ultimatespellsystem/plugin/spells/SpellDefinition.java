package fr.jamailun.ultimatespellsystem.plugin.spells;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.spells.SpellMetadata;
import fr.jamailun.ultimatespellsystem.api.utils.MultivaluedMap;
import fr.jamailun.ultimatespellsystem.dsl.visitor.PrintingVisitor;
import fr.jamailun.ultimatespellsystem.dsl.UltimateSpellSystemDSL;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.validators.DslValidator;
import fr.jamailun.ultimatespellsystem.plugin.runner.builder.SpellBuilderVisitor;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.MetadataNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

/**
 * A spell definition is the common implementation for a {@link fr.jamailun.ultimatespellsystem.api.spells.Spell}.
 */
public class SpellDefinition extends AbstractSpell {

    private final File file;
    private final List<RuntimeStatement> steps = new ArrayList<>();
    private final MultivaluedMap<String, MetadataNode> metadata = new MultivaluedMap<>();

    /**
     * Create a new spell definition.
     * @param file the source file.
     * @param name the name of the spell.
     * @param steps the steps to run.
     */
    public SpellDefinition(@NotNull File file, @NotNull String name, @NotNull List<RuntimeStatement> steps) {
        super(name);
        this.file = file;
        // Metadata are already sorted (thanks to AST validation)
        for(RuntimeStatement statement : steps) {
            if(statement instanceof MetadataNode meta) {
                metadata.put(meta.getName(), meta);
            } else {
                this.steps.add(statement);
            }
        }
        // meta reading
        if(metadata.containsKey("name")) {
            super.name = metadata.getFirst("name").getFirst(String.class);
        }
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
            return new SpellDefinition(file, name, steps);
        } catch(Exception e) {
            UltimateSpellSystem.logError("In "+file+" : " + e.getMessage());
            for(StackTraceElement se : e.getStackTrace()) {
                UltimateSpellSystem.logDebug("  §c" + se.toString());
            }
            return null;
        }
    }

    public static @NotNull String debugFile(@NotNull File file) {
        if(!file.exists())
            return "file["+file+"] doesn't exist.";
        try {
            List<StatementNode> dsl = UltimateSpellSystemDSL.parse(file);
            DslValidator.validateDsl(dsl);
            return PrintingVisitor.toString(dsl);
        } catch(Exception e) {
            UltimateSpellSystem.logError("In "+file+" : " + e.getMessage());
            return "";
        }
    }

    @Override
    protected boolean castSpell(@NotNull SpellEntity caster, @NotNull SpellRuntime runtime) {
        String prefix = "SpellRun-" + UUID.randomUUID().toString().substring(20) + " | ";

        UltimateSpellSystem.logDebug(prefix + " Casted on " + caster);

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

    @Override
    public @NotNull String getDebugString() {
        return debugFile(file);
    }

    @Override
    public @NotNull MultivaluedMap<String, SpellMetadata> getMetadata() {
        return metadata.map(SpellMetadata.class::cast);
    }

    @Override
    public String toString() {
        return "SpellDef(" + name + ")";
    }
}
