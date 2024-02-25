package fr.jamailun.ultimatespellsystem.runner.nodes.expressions;

import fr.jamailun.ultimatespellsystem.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.runner.SpellRuntime;

public class NullLiteral extends RuntimeExpression {

    @Override
    public Void evaluate(SpellRuntime runtime) {
        return null;
    }
}
