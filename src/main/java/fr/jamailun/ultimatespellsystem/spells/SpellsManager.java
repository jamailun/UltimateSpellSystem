package fr.jamailun.ultimatespellsystem.spells;

import fr.jamailun.ultimatespellsystem.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.runner.SpellDefinition;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SpellsManager {

    private final Map<String, SpellDefinition> spells = new HashMap<>();
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
            SpellDefinition definition = SpellDefinition.loadFile(child);
            if(definition != null)
                spells.put(child.getName(), definition);
        }

        UltimateSpellSystem.logInfo("Loaded " + spells.size() + " spells.");
    }

    /**
     * Get all spell IDs.
     * @return a <b>copy</b> of the spell IDs.
     */
    public List<String> spellIds() {
        return List.copyOf(spells.keySet());
    }

    public SpellDefinition getSpell(String name) {
        return spells.get(name);
    }

}
