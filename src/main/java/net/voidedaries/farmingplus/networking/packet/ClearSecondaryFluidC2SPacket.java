package net.voidedaries.farmingplus.networking.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.block.entity.FermentationBarrelBlockEntity;

@SuppressWarnings("ClassCanBeRecord")
public class ClearSecondaryFluidC2SPacket {
    public static final Identifier ID = new Identifier(FarmingPlus.MOD_ID, "clear_secondary_fluid");

    public final BlockPos pos;

    public ClearSecondaryFluidC2SPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void send(BlockPos pos) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        ClientPlayNetworking.send(ID, buf);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler ignoredHandler,
                              PacketByteBuf buf, PacketSender ignoredResponseSender) {
        BlockPos pos = buf.readBlockPos();

        server.execute(() -> {
            BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);
            if (blockEntity instanceof FermentationBarrelBlockEntity barrel) {
                barrel.secondaryFluidStorage.variant = FluidVariant.blank();
                barrel.secondaryFluidStorage.amount = 0;

                barrel.sendSecondaryFluidPacket();
            }
        });
    }
}
