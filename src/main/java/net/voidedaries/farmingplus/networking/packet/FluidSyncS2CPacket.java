package net.voidedaries.farmingplus.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.voidedaries.farmingplus.block.entity.BottlerBlockEntity;
import net.voidedaries.farmingplus.block.entity.FermentationBarrelBlockEntity;
import net.voidedaries.farmingplus.screen.BottlerScreenHandler;
import net.voidedaries.farmingplus.screen.FermentationBarrelScreenHandler;
import net.voidedaries.farmingplus.util.FluidStack;

public class FluidSyncS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        FluidVariant variant = FluidVariant.fromPacket(buf);
        long fluidLevel = buf.readLong();
        BlockPos position = buf.readBlockPos();

        client.execute(() -> {
            if (client.world != null) {
                BlockEntity blockEntity = client.world.getBlockEntity(position);
                if (blockEntity instanceof FermentationBarrelBlockEntity fermentationBarrelBlockEntity) {
                    fermentationBarrelBlockEntity.setPrimaryFluidLevel(variant, fluidLevel);

                    if (client.player.currentScreenHandler instanceof FermentationBarrelScreenHandler fermentationBarrelScreenHandler &&
                            fermentationBarrelScreenHandler.blockEntity.getPos().equals(position)) {
                        fermentationBarrelScreenHandler.setPrimaryFluid(new FluidStack(variant, fluidLevel));
                    }
                }
            }
        });
    }

    public static void receiveSecondary(MinecraftClient client, ClientPlayNetworkHandler handler,
                                        PacketByteBuf buf, PacketSender responseSender) {
        BlockPos position = buf.readBlockPos();
        FluidVariant variant = FluidVariant.fromPacket(buf);
        long fluidLevel = buf.readLong();

        client.execute(() -> {
            if (client.world != null) {
                BlockEntity blockEntity = client.world.getBlockEntity(position);
                if (blockEntity instanceof FermentationBarrelBlockEntity fermentationBarrelBlockEntity) {
                    fermentationBarrelBlockEntity.setSecondaryFluidLevel(variant, fluidLevel);

                    if (client.player.currentScreenHandler instanceof FermentationBarrelScreenHandler screenHandler &&
                            screenHandler.blockEntity.getPos().equals(position)) {
                        screenHandler.setSecondaryFluid(new FluidStack(variant, fluidLevel));
                    }
                }
            }
        });
    }

    public static void receiveBottlerSync(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        FluidVariant variant = FluidVariant.fromPacket(buf);
        long fluidLevel = buf.readLong();
        BlockPos position = buf.readBlockPos();

        client.execute(() -> {
            if (client.world != null) {
                BlockEntity blockEntity = client.world.getBlockEntity(position);
                if (blockEntity instanceof BottlerBlockEntity BottlerBlockEntity) {
                    BottlerBlockEntity.setFluidLevel(variant, fluidLevel);

                    if (client.player.currentScreenHandler instanceof
                            BottlerScreenHandler BottlerScreenHandler &&
                            BottlerScreenHandler.blockEntity.getPos().equals(position)) {
                        BottlerScreenHandler.setFluid(new FluidStack(variant, fluidLevel));
                    }

                }
            }
        });
    }

}