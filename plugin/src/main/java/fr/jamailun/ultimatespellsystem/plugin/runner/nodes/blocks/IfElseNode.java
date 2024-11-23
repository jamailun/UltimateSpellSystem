package fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import org.jetbrains.annotations.NotNull;

public class IfElseNode extends RuntimeStatement {

    private final RuntimeExpression condition;
    private final RuntimeStatement childTrue, childFalse;

    public IfElseNode(RuntimeExpression condition, RuntimeStatement childTrue, RuntimeStatement childFalse) {
        this.condition = condition;
        this.childTrue = childTrue;
        this.childFalse = childFalse;
    }

    @Override
    public void run(@NotNull SpellRuntime runtime) {
        Boolean condition = runtime.safeEvaluate(this.condition, Boolean.class);
        if(condition != null && condition)
            childTrue.run(runtime);
        else if(childFalse != null)
            childFalse.run(runtime);
    }

    @Override
    public String toString() {
        return "IF("+condition+"): " + childTrue + (childFalse == null ? "" : " ELSE: " + childFalse);
    }
}
