package fr.jamailun.ultimatespellsystem.bukkit.spells;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.dsl.UltimateSpellSystemDSL;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.validators.DslValidator;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.runner.builder.SpellBuilderVisitor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class SpellDefinition extends Spell {

    private final List<RuntimeStatement> steps;

    public SpellDefinition(String name, List<RuntimeStatement> steps) {
        super(name);
        this.steps = steps;
    }

    public static @Nullable SpellDefinition loadFile(File file) {
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

    public static @Nullable SpellDefinition loadFile(String name, File file) {
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
    protected void castSpell(@NotNull LivingEntity caster) {
        String prefix = "SpellRun-" + UUID.randomUUID().toString().substring(20) + " | ";

        UltimateSpellSystem.logDebug(prefix + " Casted on " + caster);

        SpellRuntime runtime = new SpellRuntime(caster);
        for(RuntimeStatement statement : steps) {
            UltimateSpellSystem.logDebug(prefix + "Running " + statement.toString());
            statement.run(runtime);

            if(runtime.isStopped())
                break;
        }

        UltimateSpellSystem.logDebug(prefix + "End of cast on " + caster);
    }
}
