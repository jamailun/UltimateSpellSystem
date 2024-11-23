package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.PotionEffect;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;
import fr.jamailun.ultimatespellsystem.dsl.visitor.ExpressionVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * A raw {@link org.bukkit.potion.PotionEffect} literal.
 */
public class EffectTypeExpression extends LiteralExpression<PotionEffect> {

    private final PotionEffect effect;

    public EffectTypeExpression(TokenPosition position, PotionEffect effect) {
        super(position);
        this.effect = effect;
    }

    @Override
    public PotionEffect getRaw() {
        return effect;
    }

    @Override
    public @NotNull Type getExpressionType() {
        return TypePrimitive.EFFECT_TYPE.asType();
    }

    @Override
    public String toString() {
        return PREFIX + "EffectType." + effect + SUFFIX;
    }

    @Override
    public void visit(@NotNull ExpressionVisitor visitor) {
        visitor.handleEffectLiteral(this);
    }

}
