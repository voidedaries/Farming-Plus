package net.voidedaries.farmingplus.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.voidedaries.farmingplus.mixininterface.StatusSavingPlayer;

public class IntoxicationEffect extends StatusEffect {

    public IntoxicationEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);

    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);

        if (entity instanceof StatusSavingPlayer sPlayer) {
            sPlayer.farmingplus$save();
        }
    }

    @Override
    public boolean isInstant() {
        return false;
    }
}
