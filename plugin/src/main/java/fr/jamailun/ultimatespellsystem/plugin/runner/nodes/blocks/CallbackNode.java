package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.errors.InvalidTypeException;
import fr.jamailun.ultimatespellsystem.dsl.objects.CallbackEvent;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Used to react to world events.
 */
@RequiredArgsConstructor
public class CallbackNode extends RuntimeStatement {

    private final String variableNameInput;
    private final CallbackEvent type;
    private final String variableNameArgument;
    private final RuntimeStatement child;

    @Override
    public void run(@NotNull SpellRuntime runtimeParent) {
        SpellRuntime runtime = runtimeParent.makeChild();

        // Find entity
        SpellEntity entity = runtime.variables().get(variableNameInput, SpellEntity.class);
        if(entity == null)
            throw new InvalidTypeException("callback to variable " + variableNameInput, "Entity is null.");

        // Register listener!

        //child.run(runtime);
    }
}
