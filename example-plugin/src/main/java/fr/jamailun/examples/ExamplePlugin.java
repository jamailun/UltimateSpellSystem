package fr.jamailun.examples;

import fr.jamailun.examples.citizens.CasterTrait;
import fr.jamailun.examples.commands.NpcTagCommand;
import fr.jamailun.examples.commands.SpellCasterCommand;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import net.citizensnpcs.api.util.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class ExamplePlugin extends JavaPlugin {
    private static ExamplePlugin instance;
    private static final DecimalFormat FORMAT = new DecimalFormat("#");

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
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(CasterTrait.class));

        // Register a custom placeholder, to debug things
        Placeholders.registerNPCPlaceholder(Pattern.compile("health"), (npc, sender, label) -> {
            if(npc.getEntity() instanceof LivingEntity le) {
                return FORMAT.format(le.getHealth());
            }
            return "0";
        });

        // Command
        new NpcTagCommand();
        new SpellCasterCommand();

        info("Example USS loaded.");
    }

    public static void info(String msg) {
        instance.getLogger().info(msg);
    }
    public static void error(String msg) {
        instance.getLogger().severe(msg);
    }
}
