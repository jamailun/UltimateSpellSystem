package fr.jamailun.ultimatespellsystem.dsl.visitor;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.AllEntitiesAround;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.ArrayConcatExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.PropertiesExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.VariableExpression;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.statements.*;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Map;

/**
 * A visitor. Can interact with the node-tree.
 */
public interface ExpressionVisitor {

    void handleNullLiteral(NullExpression literal);
    void handleBooleanLiteral(BooleanExpression literal);
    void handleNumberLiteral(NumberExpression literal);
    void handleStringLiteral(StringExpression literal);
    void handleEntityTypeLiteral(EntityTypeExpression literal);
    void handleRuntimeLiteral(RuntimeLiteral literal);
    void handleDurationLiteral(DurationExpression literal);
    void handleEffectLiteral(EffectTypeExpression literal);

    void handlePropertiesSet(PropertiesExpression expression);
    void handleAllAround(AllEntitiesAround expression);
    void handleArrayConcat(ArrayConcatExpression expression);
    void handleVariable(VariableExpression expression);


}
