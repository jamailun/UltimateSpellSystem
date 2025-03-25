package fr.jamailun.examples;

import fr.jamailun.examples.citizens.CasterTrait;
import fr.jamailun.examples.commands.NpcCastCommand;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {
    private static ExamplePlugin instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        if(!UltimateSpellSystem.isValid()) {
            error("USS not loaded. Stopping.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        Plugin citizens = getServer().getPluginManager().getPlugin("Citizens");
        if (citizens == null || !citizens.isEnabled()) {
            error("Citizens 2.0 not found or not enabled");
            return;
        }
        CitizensAPI.getTraitFactory()
                .registerTrait(TraitInfo.create(CasterTrait.class));

        new NpcCastCommand();

        info("TEST enabled.");
    }

    public static void info(String msg) {
        instance.getLogger().info(msg);
    }
    public static void error(String msg) {
        instance.getLogger().severe(msg);
    }
}
