package fr.jamailun.ultimatespellsystem.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.PotionEffect;
import fr.jamailun.ultimatespellsystem.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.runner.SpellRuntime;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class SendEffectNode extends RuntimeStatement {

    private final RuntimeExpression targetRef;
    private final RuntimeExpression effectRef;
    private final RuntimeExpression durationRef;
    private final RuntimeExpression optPowerRef;

    public SendEffectNode(RuntimeExpression target, RuntimeExpression effectRef, RuntimeExpression durationRef, @Nullable RuntimeExpression optPowerRef) {
        this.targetRef = target;
        this.effectRef = effectRef;
        this.durationRef = durationRef;
        this.optPowerRef = optPowerRef;
    }

    @Override
    public void run(SpellRuntime runtime) {
        LivingEntity target = runtime.safeEvaluate(targetRef, LivingEntity.class);
        PotionEffect effect = runtime.safeEvaluate(effectRef, PotionEffect.class);
        Duration duration = runtime.safeEvaluate(durationRef, Duration.class);
        int durationSecs = (int) duration.toSeconds();
        int power = 1;
        if(optPowerRef != null) {
            power = runtime.safeEvaluate(optPowerRef, Double.class).intValue();
        }
        //TODO create potion effect :)
        // getByName ....
        target.addPotionEffect(new org.bukkit.potion.PotionEffect(null, durationSecs, power));
    }
}
