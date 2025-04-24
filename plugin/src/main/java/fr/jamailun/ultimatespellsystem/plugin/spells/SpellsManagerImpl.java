package fr.jamailun.ultimatespellsystem.plugin.spells;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.spells.Spell;
import fr.jamailun.ultimatespellsystem.api.spells.SpellsManager;
import fr.jamailun.ultimatespellsystem.plugin.spells.functions.SpellFunction;
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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A global manager to access spells.
 */
public final class SpellsManagerImpl implements SpellsManager {

    private final Map<String, Spell> spells = new HashMap<>();
    private final File spellsFolder;

    private final Map<String, SpellFunction> functions = new HashMap<>();
    private final File functionsFolder;

    public SpellsManagerImpl(@NotNull File rootDirectory) {
        this.spellsFolder = new File(rootDirectory, "spells");
        this.functionsFolder = new File(rootDirectory, "functions");
        if(! (spellsFolder.exists() || spellsFolder.mkdirs())) {
            UssLogger.logError("Cannot access spells-folder " + spellsFolder + ".");
            return;
        }
        if(! (functionsFolder.exists() || functionsFolder.mkdirs())) {
            UssLogger.logError("Cannot access functions-folder " + functionsFolder + ".");
            return;
        }

        reloadSpells();
    }

    @Override
    public void reloadSpells() {
        reloadFunctions();

        UssLogger.logInfo("Reloading spells.");
        spells.clear();

        iterateDirectory(
            spellsFolder,
            SpellDefinition::loadFile,
            spell -> spells.put(spell.getName(), spell)
        );

        UssLogger.logInfo("Loaded " + spells.size() + " spells.");
    }

    private void reloadFunctions() {
        UssLogger.logInfo("Reloading spells.");
        functions.clear();
        iterateDirectory(
                functionsFolder,
                SpellFunction::loadFile,
                func -> functions.put(func.getName(), func)
        );
        UssLogger.logInfo("Loaded " + functions.size() + " functions.");
    }

    private static <T> void iterateDirectory(@NotNull File directory, Function<File, T> mapper, Consumer<T> action) {
        try(Stream<Path> fileStream = Files.walk(Paths.get(directory.getAbsolutePath()))) {
            fileStream.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(f -> !(f.getName().endsWith(".off") || f.getName().endsWith(".disabled")))
                    .filter(f -> !(f.getName().startsWith(".")))
                    .map(mapper)
                    .filter(Objects::nonNull)
                    .forEach(action);
        } catch(IOException e) {
            UssLogger.logError("Could not list files of " + directory + ": " + e.getMessage());
        }
    }

    @Override
    public boolean registerSpell(@NotNull Spell spell) {
        if(spell.getName().isBlank() || spell.getName().isEmpty()) {
            UssLogger.logWarning("Cannot register custom Spell " + spell + " : empty name.");
            return false;
        }
        if(spells.containsKey(spell.getName())) {
            UssLogger.logWarning("Cannot register custom Spell " + spell.getName() + " : duplicate name.");
            return false;
        }
        spells.put(spell.getName(), spell);
        UssLogger.logDebug("Registered custom spell '" + spell.getName() + "'.");
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
