package fr.jamailun.ultimatespellsystem.runner.builder;

import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.AllEntitiesAround;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.ArrayConcatExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.PropertiesExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.VariableExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import fr.jamailun.ultimatespellsystem.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.runner.nodes.literals.NullLiteral;
import fr.jamailun.ultimatespellsystem.runner.nodes.literals.SpellLiteral;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ExpressionQueue implements ExpressionVisitor {

    private final Deque<RuntimeExpression> expressions = new ArrayDeque<>();

    ExpressionQueue() {}

    RuntimeExpression fetch() {
        return expressions.pop();
    }

    List<RuntimeExpression> fetchAll() {
        List<RuntimeExpression> all = new ArrayList<>();
        while( ! expressions.isEmpty())
            all.add(fetch());
        return all;
    }

    @Override
    public void handlePropertiesSet(PropertiesExpression expression) {

    }

    @Override
    public void handleAllAround(AllEntitiesAround expression) {

    }

    @Override
    public void handleArrayConcat(ArrayConcatExpression expression) {

    }

    @Override
    public void handleVariable(VariableExpression expression) {

    }



    @Override
    public void handleNullLiteral(NullExpression literal) {
        expressions.add(new NullLiteral());
    }

    @Override
    public void handleBooleanLiteral(BooleanExpression literal) {
        expressions.add(new SpellLiteral<>(literal));
    }

    @Override
    public void handleNumberLiteral(NumberExpression literal) {
        expressions.add(new SpellLiteral<>(literal));
    }

    @Override
    public void handleStringLiteral(StringExpression literal) {
        expressions.add(new SpellLiteral<>(literal));
    }

    @Override
    public void handleEntityTypeLiteral(EntityTypeExpression literal) {
        expressions.add(new SpellLiteral<>(literal));
    }

    @Override
    public void handleRuntimeLiteral(RuntimeLiteral literal) {
        expressions.add(new SpellLiteral<>(literal));
    }

    @Override
    public void handleDurationLiteral(DurationExpression literal) {
        expressions.add(new SpellLiteral<>(literal));
    }

    @Override
    public void handleEffectLiteral(EffectTypeExpression literal) {
        expressions.add(new SpellLiteral<>(literal));
    }

}
