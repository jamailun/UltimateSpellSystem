package fr.jamailun.ultimatespellsystem.extension.citizens;

import fr.jamailun.ultimatespellsystem.UssLogger;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class CitizensExtensionLoader {
    private static boolean initialized = false;

    public static void initialize() {
        if(initialized) return;
        initialized = true;

        Plugin citizens = Bukkit.getServer().getPluginManager().getPlugin("Citizens");
        if (citizens == null || !citizens.isEnabled()) {
            SpellCasterCommand.initializeFakeCommand();
            return;
        }

        UssLogger.logInfo("Citizens 2.0 found. Registering extension.");

        // Trait
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(CasterTrait.class));

        // Command
        SpellCasterCommand.initialize();
    }

}
