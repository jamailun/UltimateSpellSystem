package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Duration;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;

public class DurationExpression extends LiteralExpression<Duration> {

    private final Duration duration;

    public DurationExpression(Token token) {
        super(token.pos());
        this.duration = new Duration(token.getContentNumber(), token.getContentTimeUnit());
    }

    @Override
    public Duration getRaw() {
        return duration;
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.DURATION.asType();
    }

    @Override
    public String toString() {
        return PREFIX + duration.amount() + " " + duration.timeUnit() + SUFFIX;
    }

}
