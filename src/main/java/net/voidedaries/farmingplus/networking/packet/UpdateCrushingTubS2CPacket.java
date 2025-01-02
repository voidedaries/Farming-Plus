package net.voidedaries.farmingplus.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.block.custom.CrushingTub;
import net.voidedaries.farmingplus.block.entity.CrushingTubBlockEntity;

public class UpdateCrushingTubS2CPacket {
    public static final Identifier ID = new Identifier(FarmingPlus.MOD_ID, "update_crushing_tub");

    public static void send(ServerPlayerEntity player, CrushingTubBlockEntity entity) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(entity.getPos());
        buf.writeItemStack(entity.getStack(CrushingTubBlockEntity.SLOT_1));
        buf.writeInt(entity.getGrapeFluidAmount());
        buf.writeEnumConstant(entity.getFluidType());
        ServerPlayNetworking.send(player, ID, buf);

    }

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler ignoredHandler,
                                                          PacketByteBuf buf, PacketSender ignoredResponseSender) {
        BlockPos pos = buf.readBlockPos();
        ItemStack stack = buf.readItemStack();
        int fluidLevel = buf.readInt();
        CrushingTub.GrapeFluidType fluidType = buf.readEnumConstant(CrushingTub.GrapeFluidType.class);

        client.execute(() -> {
            if (client.world != null && client.world.getBlockEntity(pos) instanceof
                    CrushingTubBlockEntity blockEntity) {

                blockEntity.setStack(CrushingTubBlockEntity.SLOT_1, stack);
                blockEntity.setGrapeFluidAmount(fluidLevel);

                blockEntity.setFluidType(fluidType);

                BlockState currentState = client.world.getBlockState(pos);
                int calculatedFluidLevel = blockEntity.calculateFluidLevel();
                BlockState newState = currentState
                        .with(CrushingTub.FLUID_LEVEL, calculatedFluidLevel)
                        .with(CrushingTub.FLUID_TYPE, fluidType);

                if (!newState.equals(currentState)) {
                    client.world.setBlockState(pos, newState, Block.NOTIFY_LISTENERS | Block.NOTIFY_ALL);
                }

            }
        });

    }

}
