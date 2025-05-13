package fr.jamailun.ultimatespellsystem.plugin.configuration;

import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurationStore;
import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
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

    private static final String PLUGIN_CONFIG_VERSION = "1.5";

    private final File file;
    private final YamlConfigurationStore<MainConfigurationVersion1> store;
    private MainConfiguration config;

    public UssConfig(@NotNull Plugin plugin) {
        file = new File(plugin.getDataFolder(), "config.yml");

        YamlConfigurationProperties properties = YamlConfigurationProperties.newBuilder()
                .inputNulls(true)
                .outputNulls(true)
                .build();
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
    public Duration getDefaultCooldown() {
        return config.getDefaultCooldown();
    }

    public long getTicksAggroSummons() {
        return config.getTickAggroSummons();
    }
    public int getTicksDefaultCustomEntity() {
        return config.getTickDefaultCustomEntity();
    }

    public void checkVersionAndMigrate() {
        String version = file.exists() ? YamlConfiguration.loadConfiguration(file).getString("version") : null;
        UssLogger.logInfo("Read configuration version: " + version);
        if(PLUGIN_CONFIG_VERSION.compareTo(Objects.requireNonNullElse(version, "0")) > 0) {
            try {
                var value = store.load(file.toPath());
                value.setVersion(PLUGIN_CONFIG_VERSION);
                value.checkDefaults();
                store.save(value, file.toPath());
                UssLogger.logWarning("Configuration overwritten using config version " + PLUGIN_CONFIG_VERSION + ".");
            } catch(Exception ignored) {
                UssLogger.logWarning("Configuration could not be read. It has been reset.");
                store.save(new MainConfigurationVersion1(), file.toPath());
            }
        }
    }

    public boolean shouldCancelStep() {
        return config.cancelOnStep();
    }
    public boolean shouldCancelCast() {
        return config.cancelOnCast();
    }
    public boolean cooldownOnMaterial() {
        return config.addCooldownToMaterial();
    }
    public @NotNull String messageOnCooldown() {
        return config.messageOnCooldown();
    }
}
