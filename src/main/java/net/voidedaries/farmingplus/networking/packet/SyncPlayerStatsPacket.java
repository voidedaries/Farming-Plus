package net.voidedaries.farmingplus.networking.packet;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.mixininterface.StatusSavingPlayer;

public class SyncPlayerStatsPacket implements FabricPacket {
    public final int storedHealth;
    public final int storedSaturation;
    public final int storedHunger;
    public final int wineCount;

    public int PlayerID;

    public static void send(ServerPlayerEntity player) {
        var statusSavingPlayer = (StatusSavingPlayer) player;

        ServerPlayNetworking.send(player, new SyncPlayerStatsPacket(player));
    }

    public static final PacketType<SyncPlayerStatsPacket> TYPE = PacketType.create(new Identifier(FarmingPlus.MOD_ID, "sync_packet"),
            SyncPlayerStatsPacket::new);

    public SyncPlayerStatsPacket(ServerPlayerEntity player) {
        PlayerID = player.getId();

        StatusSavingPlayer sPlayer = (StatusSavingPlayer) player;

        storedHealth = (sPlayer.farmingplus$getStoredHealth() != null) ? sPlayer.farmingplus$getStoredHealth() : -1;
        storedHunger = (sPlayer.farmingplus$getStoredHunger() != null) ? sPlayer.farmingplus$getStoredHunger() : -1;
        storedSaturation = (sPlayer.farmingplus$getStoredSaturation() != null) ? sPlayer.farmingplus$getStoredSaturation() : -1;
        wineCount = (sPlayer.farmingplus$getWineCount() != null) ? sPlayer.farmingplus$getWineCount() : -1;

    }

    public SyncPlayerStatsPacket(PacketByteBuf buf) {

        storedHealth = buf.readInt();
        storedHunger = buf.readInt();
        storedSaturation = buf.readInt();
        wineCount = buf.readInt();

        PlayerID = buf.readInt();

    }

    @Override
    public void write(PacketByteBuf buf) {

        buf.writeInt(storedHealth);

        buf.writeInt(storedHunger);

        buf.writeInt(storedSaturation);

        buf.writeInt(wineCount);

        buf.writeInt(PlayerID);

    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler,
                              PacketByteBuf buf, PacketSender responseSender) {

        SyncPlayerStatsPacket packet = new SyncPlayerStatsPacket(buf);


        client.execute(() -> {
            if (client.world.getEntityById(packet.PlayerID) instanceof PlayerEntity player) {
                var sPlayer = (StatusSavingPlayer) player;

                sPlayer.farmingplus$setStoredHealth(packet.storedHealth==-1?null: packet.storedHealth);

                sPlayer.farmingplus$setStoredHunger(packet.storedHunger==-1?null: packet.storedHunger);

                sPlayer.farmingplus$setStoredSaturation(packet.storedSaturation==-1?null: packet.storedSaturation);

                sPlayer.farmingplus$setWineCount(packet.wineCount == -1 ? null : packet.wineCount);
            }
        });
    }
}
