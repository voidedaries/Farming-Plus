package net.voidedaries.farmingplus.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.voidedaries.farmingplus.effect.ModEffects;
import net.voidedaries.farmingplus.mixininterface.StatusSavingPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "tickStatusEffects", at = @At("TAIL"))
    protected void farmingplus$onTickStatusEffects(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof PlayerEntity player) {
            if (!player.hasStatusEffect(ModEffects.INTOXICATION)) {
                ((StatusSavingPlayer) player).farmingplus$setWineCount(0);
            }
        }
    }

}
