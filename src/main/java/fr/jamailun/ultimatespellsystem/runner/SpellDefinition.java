package fr.jamailun.ultimatespellsystem.runner;

import fr.jamailun.ultimatespellsystem.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.dsl.UltimateSpellSystemDSL;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.runner.builder.SpellBuilderVisitor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class SpellDefinition {

    private final String name;
    private final List<RuntimeStatement> steps;
    private boolean enabled = true;

    public SpellDefinition(String name, List<RuntimeStatement> steps) {
        this.name = name;
        this.steps = steps;
    }

    public static @Nullable SpellDefinition loadFile(File file) {
        String name = file.getName()
                .replace(" ", "-")
                .toLowerCase()
                .replaceAll("\\..*", "");
        UltimateSpellSystem.logDebug("Extracted '"+name+"' from name '" + file.getName()+"'.");
        return loadFile(name, file);
    }

    public static @Nullable SpellDefinition loadFile(String name, File file) {
        try {
            List<StatementNode> dsl = UltimateSpellSystemDSL.parse(file);
            List<RuntimeStatement> steps = SpellBuilderVisitor.build(dsl);
            return new SpellDefinition(name, steps);
        } catch(Exception e) {
            UltimateSpellSystem.logError("In "+file+" : " + e.getMessage());
            return null;
        }
    }

    public void cast(@NotNull Player player) {
        String prefix = "SpellRun-" + UUID.randomUUID().toString().substring(20) + " | ";

        UltimateSpellSystem.logDebug(prefix + " Casted on " + player);

        SpellRuntime runtime = new SpellRuntime(player);
        for(RuntimeStatement statement : steps) {
            UltimateSpellSystem.logDebug(prefix + "Running " + statement.toString());
            statement.run(runtime);
        }

        UltimateSpellSystem.logDebug(prefix + "End of cast on " + player);

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        UltimateSpellSystem.logInfo("Spell '" + name + "' has been " + (enabled?"enabled":"disabled") + ".");
    }

    public String getName() {
        return name;
    }
}
