package fr.jamailun.ultimatespellsystem.plugin.bind.costs;

import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * More like a condition. A {@link Player} must have a required permission.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PermissionCost implements SpellCost {

    private String permission;

    public PermissionCost(@NotNull List<String> serialized) {
        this.permission =serialized.getFirst();
    }

    @Override
    public boolean canPay(@NotNull SpellEntity caster) {
        if(caster.getBukkitEntity().orElse(null) instanceof Player player) {
            return player.hasPermission(permission);
        }
        return true;
    }

    @Override
    public void pay(@NotNull SpellEntity caster) {
        // Nothing here.
    }

    @Override
    public @NotNull List<Object> serialize() {
        return List.of(permission);
    }

    @Override
    public String toString() {
        return "Permission[" + permission + "]";
    }
}
