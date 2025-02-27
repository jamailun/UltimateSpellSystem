package fr.jamailun.ultimatespellsystem.plugin.spells;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.api.spells.SpellsManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

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
        UltimateSpellSystem.logInfo("Reloading spells.");
        spells.clear();

        try(Stream<Path> fileStream = Files.walk(Paths.get(spellsFolder.getAbsolutePath()))) {
            fileStream.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(f -> !(f.getName().endsWith(".off") || f.getName().endsWith(".disabled")))
                    .map(SpellDefinition::loadFile)
                    .filter(Objects::nonNull)
                    .forEach(spell -> spells.put(spell.getName(), spell));
        } catch(IOException e) {
            UltimateSpellSystem.logError("Could not list files of " + spellsFolder + ": " + e.getMessage());
            return;
        }

        UltimateSpellSystem.logInfo("Loaded " + spells.size() + " spells.");
    }

    @Override
    public boolean registerSpell(@NotNull Spell spell) {
        if(spell.getName().isBlank() || spell.getName().isEmpty()) {
            UltimateSpellSystem.logWarning("Cannot register custom Spell " + spell + " : empty name.");
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
