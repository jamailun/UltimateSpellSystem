package fr.jamailun.ultimatespellsystem.extension.providers;

import fr.jamailun.ultimatespellsystem.api.providers.SummonPropertiesProvider;
import fr.jamailun.ultimatespellsystem.plugin.utils.TypeInterpretation;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

/**
 * Custom attributes for entities.
 */
public final class EntityAttributes {

    public static void register() {
        registerProjectileVelocityAndDirection();
    }

    private static void registerProjectileVelocityAndDirection() {
        SummonPropertiesProvider.instance().register(
                SummonPropertiesProvider.createForEntity(
                        (projectile, speed, ctx) -> {
                            // If velocity is non-null, then we'll set the direction.
                            if(speed != 0) {
                                Vector dir;
                                // If the "direction" attribute is set, use it.
                                // If not, the projectile will use the caster direction.
                                if(ctx.attributes().hasAttribute("direction")) {
                                    dir = TypeInterpretation.extractDirection(ctx.attributes().getAttribute("direction"));
                                } else {
                                    dir = ctx.attributes().getSummoner().getLocation().getDirection().clone();
                                }
                                Vector velocity = dir.normalize().multiply(speed);
                                projectile.setVelocity(velocity);
                            }
                        },
                        Projectile.class,
                        Double.class
                ), "velocity", "projectile_velocity"
        );
        SummonPropertiesProvider.registerBlank("direction", "projectile_damage");
    }

}
