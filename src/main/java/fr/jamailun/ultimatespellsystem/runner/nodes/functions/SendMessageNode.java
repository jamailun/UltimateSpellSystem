package fr.jamailun.ultimatespellsystem.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.runner.SpellRuntime;
import org.bukkit.entity.LivingEntity;

public class SendMessageNode extends RuntimeStatement {

    private final RuntimeExpression targetRef;
    private final RuntimeExpression messageRef;

    public SendMessageNode(RuntimeExpression target, RuntimeExpression message) {
        this.targetRef = target;
        this.messageRef = message;
    }

    @Override
    public void run(SpellRuntime runtime) {
        LivingEntity target = runtime.safeEvaluate(targetRef, LivingEntity.class);
        String message = runtime.safeEvaluate(messageRef, String.class);

        target.sendPlainMessage(message);
    }
}
