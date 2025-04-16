package fr.jamailun.ultimatespellsystem.plugin.configuration;

import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurationStore;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.plugin.utils.observable.AbstractObservable;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Plugin configuration file read.
 */
public class UssConfig extends AbstractObservable<UssConfig> {

    private static final String PLUGIN_CONFIG_VERSION = "1.1";

    private final File file;
    private final YamlConfigurationStore<MainConfigurationVersion1> store;
    private MainConfiguration config;

    public UssConfig(@NotNull Plugin plugin) {
        file = new File(plugin.getDataFolder(), "config.yml");

        YamlConfigurationProperties properties = YamlConfigurationProperties.newBuilder().build();
        store = new YamlConfigurationStore<>(MainConfigurationVersion1.class, properties);
    }

    public void reload() {
        config = store.load(file.toPath());
        callObservers(this);
    }

    public boolean isDebug() {
        return config.isDebug();
    }

    public List<ItemBindTrigger> getDefaultTrigger() {
        return config.getDefaultTriggerSteps();
    }

    public SpellCost getDefaultCost() {
        return config.getDefaultSpellCost();
    }

    public long getTicksAggroSummons() {
        return config.getTickAggroSummons();
    }

    public void checkVersionAndMigrate() {
        String version = file.exists() ? YamlConfiguration.loadConfiguration(file).getString("version") : null;
        UltimateSpellSystem.logInfo("Read configuration version: " + version);
        if(PLUGIN_CONFIG_VERSION.compareTo(Objects.requireNonNullElse(version, "0")) > 0) {
            UltimateSpellSystem.logWarning("Resetting configuration. A proper migration system will be created later.");
            if(file.exists() && !file.delete()) UltimateSpellSystem.logError("Could not delete config file.");
            store.save(new MainConfigurationVersion1(), file.toPath());
        }
    }
}
