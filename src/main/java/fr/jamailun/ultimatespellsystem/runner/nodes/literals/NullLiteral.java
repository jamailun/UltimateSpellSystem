package fr.jamailun.ultimatespellsystem.runner.nodes.literals;

import fr.jamailun.ultimatespellsystem.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.runner.SpellRuntime;

public class NullLiteral extends RuntimeExpression {

    @Override
    public Object evaluate(SpellRuntime runtime) {
        return null;
    }
}
