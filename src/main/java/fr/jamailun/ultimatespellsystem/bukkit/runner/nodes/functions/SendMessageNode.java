package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import fr.jamailun.ultimatespellsystem.bukkit.utils.KyoriAdaptor;
import net.kyori.adventure.text.Component;

import java.util.List;

public class SendMessageNode extends RuntimeStatement {

    private final RuntimeExpression targetRef;
    private final RuntimeExpression messageRef;

    public SendMessageNode(RuntimeExpression target, RuntimeExpression message) {
        this.targetRef = target;
        this.messageRef = message;
    }

    @Override
    public void run(SpellRuntime runtime) {
        List<SpellEntity> targets = runtime.safeEvaluateAcceptsList(targetRef, SpellEntity.class);

        List<Component> messages = runtime.safeEvaluateAcceptsList(messageRef, String.class)
                .stream()
                .map(message -> replaceVars(message, runtime.makeChild()))
                .map(KyoriAdaptor::adventure)
                .toList();

        for(SpellEntity target : targets) {
            messages.forEach(target::sendMessage);
        }
    }

    private String replaceVars(String value, SpellRuntime runtime) {
        for(String varName : runtime.variables().names()) {
            value = value.replace("%" + varName, String.valueOf(runtime.variables().get(varName)));
        }
        return value;
    }

    @Override
    public String toString() {
        return "SEND TO " + targetRef + " MESSAGE " + messageRef;
    }
}
