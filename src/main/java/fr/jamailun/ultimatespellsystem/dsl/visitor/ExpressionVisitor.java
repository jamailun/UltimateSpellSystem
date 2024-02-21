package fr.jamailun.ultimatespellsystem.dsl.visitor;

import fr.jamailun.ultimatespellsystem.dsl.nodes.ExpressionNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.AllEntitiesAround;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.ArrayConcatExpression;
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

    void handleValueNull();

    void handleValue(Object value, TypePrimitive type);
    void handleArray(List<ExpressionNode> expressions, TypePrimitive type);
    void handleProperties(Map<String, ExpressionNode> properties);
    void handleVariable(String varName);


    void handleFunction(AllEntitiesAround expression);


}
