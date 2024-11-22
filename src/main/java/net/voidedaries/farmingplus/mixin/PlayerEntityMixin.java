package net.voidedaries.farmingplus.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.voidedaries.farmingplus.effect.ModEffects;
import net.voidedaries.farmingplus.mixininterface.StatusSavingPlayer;
import net.voidedaries.farmingplus.networking.packet.SyncPlayerStatsPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements StatusSavingPlayer {

    @Shadow public abstract HungerManager getHungerManager();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    @Nullable
    private Integer storedHealth = null;

    @Unique
    @Nullable
    private Integer storedHunger = null;

    @Unique
    @Nullable
    private Integer storedSaturation = null;

    @Unique
    @Nullable
    private Integer wineCount = null;

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void saveCustomData(NbtCompound nbt, CallbackInfo ci) {
        if (storedHealth != null) {
            nbt.putInt("farmingplus.storedhealth", storedHealth);
        }
        if (storedHunger != null) {
            nbt.putInt("farmingplus.storedhunger", storedHunger);
        }
        if (storedSaturation != null) {
            nbt.putInt("farmingplus.storedsaturation", storedSaturation);
        }
        if (wineCount != null) {
            nbt.putInt("farmingplus.winecount", wineCount);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void loadCustomData(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("farmingplus.storedhealth", NbtElement.INT_TYPE)) {
            storedHealth = nbt.getInt("farmingplus.storedhealth");
        }
        if (nbt.contains("farmingplus.storedhunger", NbtElement.INT_TYPE)) {
            storedHunger = nbt.getInt("farmingplus.storedhunger");
        }
        if (nbt.contains("farmingplus.storedsaturation", NbtElement.INT_TYPE)) {
            storedSaturation = nbt.getInt("farmingplus.storedsaturation");
        }
        if (nbt.contains("farmingplus.winecount", NbtElement.INT_TYPE)) {
            wineCount = nbt.getInt("farmingplus.winecount");
        }
    }

    @Override
    public @Nullable Integer farmingplus$getStoredHealth() {
        return this.storedHealth;
    }

    @Override
    public @Nullable Integer farmingplus$getStoredHunger() {
        return this.storedHunger;
    }

    @Override
    public @Nullable Integer farmingplus$getStoredSaturation() {
        return this.storedSaturation;
    }

    @Override
    public @Nullable Integer farmingplus$getWineCount() {
        return this.wineCount;
    }

    @Override
    public void farmingplus$setStoredHealth(@Nullable Integer storedHealth) {
        this.storedHealth = storedHealth;

        if ((Object) this instanceof ServerPlayerEntity serverPlayer) {
            SyncPlayerStatsPacket.send(serverPlayer);
        }
    }

    @Override
    public void farmingplus$setStoredHunger(@Nullable Integer storedHunger) {
        this.storedHunger = storedHunger;

        if ((Object) this instanceof ServerPlayerEntity serverPlayer) {
            SyncPlayerStatsPacket.send(serverPlayer);
        }
    }

    @Override
    public void farmingplus$setStoredSaturation(@Nullable Integer storedSaturation) {
        this.storedSaturation = storedSaturation;

        if ((Object) this instanceof ServerPlayerEntity serverPlayer) {
            SyncPlayerStatsPacket.send(serverPlayer);
        }
    }

    @Override
    public void farmingplus$setWineCount(@Nullable Integer wineCount) {
        this.wineCount = wineCount;

        if ((Object) this instanceof ServerPlayerEntity serverPlayer) {
            SyncPlayerStatsPacket.send(serverPlayer);
        }
    }

    @Override
    public void farmingplus$save() {
        this.storedHealth = (int) this.getHealth();

        this.storedHunger = this.getHungerManager().getFoodLevel();

        this.storedSaturation = (int) this.getHungerManager().getSaturationLevel();

        if ((Object) this instanceof ServerPlayerEntity serverPlayer) {
            SyncPlayerStatsPacket.send(serverPlayer);
        }
    }

    @Override
    public void farmingplus$clear() {

        storedHealth = null;

        storedHunger = null;

        storedSaturation = null;

        if ((Object) this instanceof ServerPlayerEntity serverPlayer) {
            SyncPlayerStatsPacket.send(serverPlayer);
        }
    }

}
