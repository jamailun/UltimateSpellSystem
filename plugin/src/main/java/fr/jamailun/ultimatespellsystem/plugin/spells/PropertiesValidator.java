package fr.jamailun.ultimatespellsystem.plugin.spells;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.providers.SummonPropertiesProvider;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.blocks.*;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.expressions.PropertiesLiteral;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.SummonNode;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

@RequiredArgsConstructor
public class PropertiesValidator {

    private final String spellId;

    static {
        SummonPropertiesProvider.registerBlank(
                // CustomEntity
                "_clock",
                // Orb
                "particle", "particles", "effect", "effects",
                "radius", "max_collisions", "max_blocks_hit", "damages",
                "fire", "apply_self", "sound", "hit_sound"
        );
    }

    public static void validateSpell(@NotNull String spellId, @NotNull Collection<RuntimeStatement> statements) {
        new PropertiesValidator(spellId).validateSteps(statements);
    }

    public void validateSteps(@NotNull Collection<RuntimeStatement> statements) {
        for(RuntimeStatement statement : statements) {
            validateStep(statement);
        }
    }

    public void validateStep(@NotNull RuntimeStatement statement) {
        switch(statement) {
            // Blocks
            case BlockNodes node -> validateSteps(node.getChildren());
            case ForLoopNode node -> validateStep(node.getChild());
            case ForeachLoopNode node -> validateStep(node.getChild());
            case WhileLoopNode node -> validateStep(node.getChild());
            case RunLaterNode node -> validateStep(node.getChild());
            case RunRepeatNode node -> validateStep(node.getChild());
            case CallbackNode node -> validateStep(node.getChild());
            case IfElseNode node -> {
                validateStep(node.getChildTrue());
                if(node.getChildFalse() != null) validateStep(node.getChildFalse());
            }
            // Expression-container
            case SummonNode node -> validatePropertiesSummon(node.getOptProperty());
            default -> {}
        }
    }

    private void validatePropertiesSummon(@Nullable RuntimeExpression expression) {
        if(!(expression instanceof PropertiesLiteral properties)) return;

        for(String key : properties.keys()) {
            if(!SummonPropertiesProvider.instance().exists(key)) {
                UssLogger.logWarning("Spell '" + spellId + "' : unknown summon-property '&c" + key + "&r'.");
            }
        }
    }

}
