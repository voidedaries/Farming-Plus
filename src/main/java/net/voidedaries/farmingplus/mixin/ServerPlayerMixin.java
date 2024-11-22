package net.voidedaries.farmingplus.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.voidedaries.farmingplus.effect.ModEffects;
import net.voidedaries.farmingplus.mixininterface.StatusSavingPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerMixin extends LivingEntityMixin{

}
