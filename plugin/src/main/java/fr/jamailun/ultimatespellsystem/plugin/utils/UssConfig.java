package fr.jamailun.ultimatespellsystem.plugin.utils;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.bind.ItemBindTrigger;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.bind.SpellCostEntry;
import fr.jamailun.ultimatespellsystem.plugin.bind.costs.NoneSpellCost;
import fr.jamailun.ultimatespellsystem.plugin.utils.observable.AbstractObservable;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Plugin configuration file read.
 */
@Getter
public class UssConfig extends AbstractObservable<UssConfig> {

    private static final String PLUGIN_CONFIG_VERSION = "1";

    private boolean debug;
    private List<ItemBindTrigger> defaultTrigger;
    private SpellCost defaultCost;

    private double checkSummonsAggroEverySeconds;

    public void reload(@NotNull ConfigurationSection config) {
        debug = config.getBoolean("debug", false);

        defaultTrigger = readDefaultTrigger(config);
        defaultCost = readDefaultSpellCost(config);

        checkSummonsAggroEverySeconds = config.getDouble("tick.aggro.summons", 5d);

        callObservers(this);
    }

    private List<ItemBindTrigger> readDefaultTrigger(ConfigurationSection config) {
        List<String> defaultTriggerRaw = config.getStringList("bind.default-trigger");

        List<ItemBindTrigger> list = new ArrayList<>();
        for(String def : defaultTriggerRaw) {
            try {
                list.add(ItemBindTrigger.valueOf(def.toUpperCase()));
            } catch(IllegalArgumentException e) {
                UltimateSpellSystem.logWarning("Config: invalid default trigger: '"+def+"'");
                return List.of(ItemBindTrigger.RIGHT_CLICK);
            }
        }
        return list;
    }

    private SpellCost readDefaultSpellCost(ConfigurationSection config) {
        String defaultCostId = config.getString("bind.default-cost.type", "none");
        SpellCostEntry<?> entry = UltimateSpellSystem.getSpellCostRegistry().get(defaultCostId);
        if(entry == null) {
            UltimateSpellSystem.logWarning("Config: unknown ");
            return new NoneSpellCost();
        }
        List<String> defaultCostArgs = config.getStringList("bind.default-cost.args");
        if(defaultCostArgs.size() < entry.args().size()) {
            UltimateSpellSystem.logWarning("Config: invalid args size. Expected arguments: " + entry.args() +", got " + defaultCostArgs);
            return new NoneSpellCost();
        }
        try {
            return entry.deserialize(defaultCostArgs);
        } catch(IllegalArgumentException e) {
            UltimateSpellSystem.logWarning("Config: could not read default default cost: '"+defaultCostId+"' with args " + defaultCostArgs);
            return new NoneSpellCost();
        }
    }

    public void checkVersionAndMigrate(File file, Runnable createDefaultConfig) {
        String version = file.exists() ? YamlConfiguration.loadConfiguration(file).getString("version") : null;
        UltimateSpellSystem.logInfo("Read configuration version: " + version);
        if(!PLUGIN_CONFIG_VERSION.equals(version)) {
            UltimateSpellSystem.logWarning("Resetting configuration. A proper migration system will be created later.");
            if(!file.delete()) UltimateSpellSystem.logError("Could not delete config file.");
            createDefaultConfig.run();
        }
    }
}
