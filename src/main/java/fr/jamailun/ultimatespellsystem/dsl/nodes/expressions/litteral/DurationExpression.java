package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.Token;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

import java.util.concurrent.TimeUnit;

public class DurationExpression extends LiteralExpression {

    private final double duration;
    private final TimeUnit unit;

    public DurationExpression(Token token) {
        super(token.pos());
        this.duration = token.getContentNumber();
        this.unit = token.getContentTimeUnit();
    }

    public double getDuration() {
        return duration;
    }
    public TimeUnit getTimeUnit() {
        return unit;
    }

    @Override
    public Type getExpressionType() {
        return TypePrimitive.DURATION.asType();
    }

    @Override
    public String toString() {
        return "{{" + duration + " " + unit + "}}";
    }
}
