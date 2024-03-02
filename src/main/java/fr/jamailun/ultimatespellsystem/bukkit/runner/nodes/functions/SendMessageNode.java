package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import fr.jamailun.ultimatespellsystem.bukkit.utils.KyoriAdaptor;
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
        SpellEntity target = runtime.safeEvaluate(targetRef, SpellEntity.class);
        String message = runtime.safeEvaluate(messageRef, String.class);

        target.sendMessage(KyoriAdaptor.adventure(message));
    }

    @Override
    public String toString() {
        return "SEND TO " + targetRef + " MESSAGE " + messageRef;
    }
}
