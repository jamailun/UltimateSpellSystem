package fr.jamailun.ultimatespellsystem.bukkit.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.api.bukkit.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.SpellRuntime;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ForeachLoopNode extends RuntimeStatement {

    private final String varName;
    private final RuntimeExpression source;
    private final RuntimeStatement child;

    public ForeachLoopNode(String varName, RuntimeExpression source, RuntimeStatement child) {
        this.varName = varName;
        this.source = source;
        this.child = child;
    }

    @Override
    public void run(@NotNull SpellRuntime runtimeParent) {
        SpellRuntime runtime = runtimeParent.makeChild();

        List<Object> list = runtime.safeEvaluateList(source, Object.class);

        for(Object object : list) {
            runtime.variables().set(varName, object);
            child.run(runtime);
            if(runtime.isStopped())
                return;
        }
    }

}
