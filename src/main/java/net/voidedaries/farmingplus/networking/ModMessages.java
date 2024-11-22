package net.voidedaries.farmingplus.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.networking.packet.ClearSecondaryFluidC2SPacket;
import net.voidedaries.farmingplus.networking.packet.FluidSyncS2CPacket;
import net.voidedaries.farmingplus.networking.packet.SyncPlayerStatsPacket;
import net.voidedaries.farmingplus.networking.packet.UpdateCrushingTubS2CPacket;

public class ModMessages {

    public static final Identifier FLUID_SYNC = new Identifier(FarmingPlus.MOD_ID, "fluid_sync");
    public static final Identifier SECONDARY_FLUID_SYNC = new Identifier(FarmingPlus.MOD_ID, "secondary_fluid_sync");
    public static final Identifier BOTTLER_FLUID_SYNC = new Identifier(FarmingPlus.MOD_ID, "bottler_fluid_sync");
    public static final Identifier UPDATE_CRUSHING_TUB_ID = new Identifier(FarmingPlus.MOD_ID, "update_crushing_tub");
    public static final Identifier CLEAR_SECONDARY_FLUID = new Identifier(FarmingPlus.MOD_ID, "clear_secondary_fluid");
    @SuppressWarnings("unused") // I NEED TO FINISH THIS
    public static final Identifier SYNC_PLAYER_STATS_ID = new Identifier(FarmingPlus.MOD_ID, "sync_player_stats");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(CLEAR_SECONDARY_FLUID, ClearSecondaryFluidC2SPacket::handle);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(FLUID_SYNC, FluidSyncS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(SECONDARY_FLUID_SYNC, FluidSyncS2CPacket::receiveSecondary);
        ClientPlayNetworking.registerGlobalReceiver(BOTTLER_FLUID_SYNC, FluidSyncS2CPacket::receiveBottlerSync);
        ClientPlayNetworking.registerGlobalReceiver(UPDATE_CRUSHING_TUB_ID, UpdateCrushingTubS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(SyncPlayerStatsPacket.TYPE.getId(), SyncPlayerStatsPacket::handle);
    }

}