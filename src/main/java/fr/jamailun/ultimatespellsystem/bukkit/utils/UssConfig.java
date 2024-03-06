package fr.jamailun.ultimatespellsystem.bukkit.utils;

import org.bukkit.configuration.ConfigurationSection;

public class UssConfig {

    public boolean debug;
    public boolean onlyRightClick;

    public void reload(ConfigurationSection config) {
        debug = config.getBoolean("debug", false);
        onlyRightClick = config.getBoolean("only-right-clicks", true);
    }

}
