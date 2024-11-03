package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.bukkit.spells.SpellEntity;
import fr.jamailun.ultimatespellsystem.bukkit.utils.StringTransformation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

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

        List<? extends Component> messages = runtime.safeEvaluateAcceptsList(messageRef, String.class)
                .stream()
                .map(message -> StringTransformation.transformString(message, runtime.makeChild()))
                .map(LegacyComponentSerializer.legacyAmpersand()::deserialize)
                .toList();

        for(SpellEntity target : targets) {
            messages.forEach(target::sendMessage);
        }
    }

    @Override
    public String toString() {
        return "SEND TO " + targetRef + " MESSAGE " + messageRef;
    }
}
