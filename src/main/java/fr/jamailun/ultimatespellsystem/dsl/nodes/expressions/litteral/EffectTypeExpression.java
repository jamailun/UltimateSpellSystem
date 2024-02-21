package fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.litteral;

import fr.jamailun.ultimatespellsystem.dsl.nodes.type.PotionEffect;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenPosition;

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
    public Type getExpressionType() {
        return TypePrimitive.EFFECT_TYPE.asType();
    }

    @Override
    public String toString() {
        return PREFIX + "EffectType." + effect + SUFFIX;
    }

}
