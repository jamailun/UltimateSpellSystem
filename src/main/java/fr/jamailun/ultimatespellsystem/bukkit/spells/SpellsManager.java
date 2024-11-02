package fr.jamailun.ultimatespellsystem.bukkit.spells;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SpellsManager {

    private final Map<String, SpellFunction> functions = new HashMap<>();
    private final Map<String, Spell> spells = new HashMap<>();
    private final File spellsFolder;

    public SpellsManager(File spellsFolder) {
        this.spellsFolder = spellsFolder;
        if(! (spellsFolder.exists() || spellsFolder.mkdirs())) {
            UltimateSpellSystem.logError("Cannot access " + spellsFolder + ".");
            return;
        }

        reloadSpells();
    }

    public void reloadSpells() {
        spells.clear();
        UltimateSpellSystem.logDebug("Reloading spells.");

        File[] children = spellsFolder.listFiles();
        if(children == null) {
            UltimateSpellSystem.logError("Could not list files of " + spellsFolder + ".");
            return;
        }
        for(File child : children) {
            Spell definition = SpellDefinition.loadFile(child);
            if(definition != null)
                spells.put(definition.getName(), definition);
        }

        UltimateSpellSystem.logInfo("Loaded " + spells.size() + " spells.");
    }

    /**
     * Register a custom spell implementation.
     * @param spell the spell to use.
     * @return false if the spell is invalid or if the name already exists. True in case of success.
     */
    public boolean registerSpell(Spell spell) {
        if(spell == null || spell.getName() == null || spell.getName().isBlank() || spell.getName().isEmpty()) {
            UltimateSpellSystem.logWarning("Cannot register custom Spell " + spell + " : null spell or null/empty name.");
            return false;
        }
        if(spells.containsKey(spell.getName())) {
            UltimateSpellSystem.logWarning("Cannot register custom Spell " + spell.getName() + " : duplicate name.");
            return false;
        }
        spells.put(spell.getName(), spell);
        UltimateSpellSystem.logDebug("Registered custom spell '" + spell.getName() + "'.");
        return true;
    }

    /**
     * Get all spell IDs.
     * @return a <b>copy</b> of the spell IDs.
     */
    public List<String> spellIds() {
        return List.copyOf(spells.keySet());
    }

    public List<Spell> spells() {
        return List.copyOf(spells.values());
    }

    public @Nullable Spell getSpell(String name) {
        return spells.get(name);
    }

    public void registerFunction(@NotNull String id, @NotNull SpellFunction function) {
        functions.put(id, function);
        UltimateSpellSystem.logDebug("Registered spell-function '" + id + "'.");
    }

    public @Nullable SpellFunction getFunction(String id) {
        return functions.get(id);
    }

}
