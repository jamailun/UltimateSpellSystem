package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.CallbackAction;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.entities.SummonAttributes;
import fr.jamailun.ultimatespellsystem.api.providers.CallbackEventProvider;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.errors.InvalidTypeException;
import fr.jamailun.ultimatespellsystem.dsl.objects.CallbackEvent;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Used to react to world events.
 */
public class CallbackNode extends RuntimeStatement {

    private final String variableNameInput;
    private final CallbackAction<?,?> action;
    private final String variableNameArgument;
    @Getter private final RuntimeStatement child;

    public CallbackNode(String variableNameInput, CallbackEvent type, String variableNameArgument, RuntimeStatement child) {
        this.variableNameInput = variableNameInput;
        this.action = CallbackEventProvider.instance().find(type).orElseThrow(() -> new RuntimeException("Could not get callback for " + type));
        this.variableNameArgument = variableNameArgument;
        this.child = child;
    }

    @Override
    public void run(@NotNull SpellRuntime runtimeParent) {
        SpellRuntime runtime = runtimeParent.makeChild();

        // Find entity
        SpellEntity entity = runtime.variables().get(variableNameInput, SpellEntity.class);
        if(entity == null)
            throw new InvalidTypeException("callback to variable " + variableNameInput, "Entity is null.");

        // Register on summon
        SummonAttributes summon = UltimateSpellSystem.getSummonsManager().find(entity.getUniqueId());
        if(summon != null) {
            action.registerToSummon(summon, variableNameArgument, runtime, child);
        }
    }

}
