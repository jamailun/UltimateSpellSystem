package fr.jamailun.ultimatespellsystem.bukkit.spells;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bukkit.spells.Spell;
import fr.jamailun.ultimatespellsystem.api.bukkit.spells.SpellsManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A global manager to access spells.
 */
public final class SpellsManagerImpl implements SpellsManager {

    private final Map<String, Spell> spells = new HashMap<>();
    private final File spellsFolder;

    public SpellsManagerImpl(@NotNull File spellsFolder) {
        this.spellsFolder = spellsFolder;
        if(! (spellsFolder.exists() || spellsFolder.mkdirs())) {
            UltimateSpellSystem.logError("Cannot access " + spellsFolder + ".");
            return;
        }

        reloadSpells();
    }

    @Override
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

    @Override
    public boolean registerSpell(@NotNull Spell spell) {
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

    @Override
    public @NotNull @Unmodifiable List<String> spellIds() {
        return List.copyOf(spells.keySet());
    }

    @Override
    public @NotNull @Unmodifiable List<Spell> spells() {
        return List.copyOf(spells.values());
    }

    @Override
    public @Nullable Spell getSpell(@NotNull String name) {
        return spells.get(name);
    }

}
