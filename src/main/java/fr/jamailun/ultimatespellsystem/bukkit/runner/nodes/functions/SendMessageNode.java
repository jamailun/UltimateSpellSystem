package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.functions;

import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.bukkit.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.bukkit.utils.StringTransformation;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class SendMessageNode extends RuntimeStatement {

    private final RuntimeExpression targetRef;
    private final RuntimeExpression messageRef;

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        List<SpellEntity> targets = runtime.safeEvaluateAcceptsList(targetRef, SpellEntity.class);

        List<? extends Component> messages = runtime.safeEvaluateAcceptsList(messageRef, String.class)
                .stream()
                .map(message -> StringTransformation.transformString(message, runtime.makeChild()))
                .map(StringTransformation::parse)
                .toList();

        for(SpellEntity target : targets) {
            messages.forEach(target::sendMessage);
        }
    }

    @Override
    public @NotNull String toString() {
        return "SEND TO " + targetRef + " MESSAGE " + messageRef;
    }
}
