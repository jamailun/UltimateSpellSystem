package fr.jamailun.ultimatespellsystem.extension.providers;

import fr.jamailun.ultimatespellsystem.UssKeys;
import fr.jamailun.ultimatespellsystem.api.providers.SummonPropertiesProvider;
import org.bukkit.entity.Projectile;
import org.bukkit.persistence.PersistentDataType;

/**
 * Custom attributes for entities.
 */
public final class EntityAttributes {

    public static void register() {
        registerProjectileDamages();
    }

    private static void registerProjectileDamages() {
        SummonPropertiesProvider.instance().register(
                SummonPropertiesProvider.createForEntity(
                        (proj, damage, run) -> {
                            // Set a custom amount of damage.
                            // A plugin-specific event will apply those damage.
                            proj.getPersistentDataContainer().set(
                                    UssKeys.getProjectileDamagesKey(),
                                    PersistentDataType.DOUBLE,
                                    damage
                            );
                        },
                        Projectile.class,
                        Double.class
                ), "projectile_damage"
        );
    }

}
