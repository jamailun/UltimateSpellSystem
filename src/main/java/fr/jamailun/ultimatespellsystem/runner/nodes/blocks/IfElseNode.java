package fr.jamailun.ultimatespellsystem.runner.nodes.blocks;

import fr.jamailun.ultimatespellsystem.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.runner.SpellRuntime;

public class IfElseNode extends RuntimeStatement {

    private final RuntimeExpression condition;
    private final RuntimeStatement childTrue, childFalse;

    public IfElseNode(RuntimeExpression condition, RuntimeStatement childTrue, RuntimeStatement childFalse) {
        this.condition = condition;
        this.childTrue = childTrue;
        this.childFalse = childFalse;
    }

    @Override
    public void run(SpellRuntime runtime) {
        Boolean condition = runtime.safeEvaluate(this.condition, Boolean.class);
        if(condition != null && condition)
            childTrue.run(runtime);
        else
            childFalse.run(runtime);
    }
}
