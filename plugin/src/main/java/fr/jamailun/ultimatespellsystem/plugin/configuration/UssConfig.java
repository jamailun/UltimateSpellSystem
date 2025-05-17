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
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Plugin configuration file read.
 */
public class UssConfig extends AbstractObservable<UssConfig> {

    private static final String PLUGIN_CONFIG_VERSION = "1.6";

    private final File file;
    private final YamlConfigurationStore<MainConfigurationVersion1> store;
    private static MainConfiguration CONFIG;

    public UssConfig(@NotNull Plugin plugin) {
        file = new File(plugin.getDataFolder(), "config.yml");

        YamlConfigurationProperties properties = YamlConfigurationProperties.newBuilder()
                .inputNulls(true)
                .outputNulls(true)
                .build();
        store = new YamlConfigurationStore<>(MainConfigurationVersion1.class, properties);
    }

    public void reload() {
        CONFIG = store.load(file.toPath());
        callObservers(this);
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

    public static boolean isDebug() {
        return CONFIG.isDebug();
    }
    public static List<ItemBindTrigger> getDefaultTrigger() {
        return CONFIG.getDefaultTriggerSteps();
    }
    public static @NotNull SpellCost getDefaultCost() {
        return CONFIG.getDefaultSpellCost();
    }
    public static @Nullable Duration getDefaultCooldown() {
        return CONFIG.getDefaultCooldown();
    }
    public static long getTicksAggroSummons() {
        return CONFIG.getTickAggroSummons();
    }
    public static int getTicksDefaultCustomEntity() {
        return CONFIG.getTickDefaultCustomEntity();
    }
    public static boolean shouldCancelStep() {
        return CONFIG.cancelOnStep();
    }
    public static boolean shouldCancelCast() {
        return CONFIG.cancelOnCast();
    }
    public static boolean cooldownOnMaterial() {
        return CONFIG.addCooldownToMaterial();
    }
    public static @NotNull String messageOnCooldown() {
        return CONFIG.messageOnCooldown();
    }
    public static boolean displaySummonWarnings() {
        return CONFIG.displaySummonWarnings();
    }
}
