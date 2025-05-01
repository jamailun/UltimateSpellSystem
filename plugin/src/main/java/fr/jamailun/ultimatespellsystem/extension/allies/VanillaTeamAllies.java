package fr.jamailun.ultimatespellsystem.extension.allies;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.providers.AlliesProvider;
import fr.jamailun.ultimatespellsystem.api.providers.AlliesProvider.AlliesResult;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

/**
 * Vanilla teams check. Players must share the same one, and have the {@link Team#allowFriendlyFire()} rule disabled.
 */
public class VanillaTeamAllies implements AlliesProvider.AlliesCheck {

    @Override
    public @NotNull AlliesResult test(@NotNull SpellEntity spellEntity, @NotNull Entity entity) {
        Entity caster = spellEntity.getBukkitEntity().orElse(null);
        if(caster == null) return AlliesResult.IGNORE;

        // Get the team, only if both entities share it
        Team teamCaster = Bukkit.getScoreboardManager().getMainScoreboard().getEntityTeam(caster);
        if(teamCaster == null || !(teamCaster.hasEntity(entity))) return AlliesResult.IGNORE;

        return teamCaster.allowFriendlyFire() ? AlliesResult.IGNORE : AlliesResult.ALLIES;
    }

}
