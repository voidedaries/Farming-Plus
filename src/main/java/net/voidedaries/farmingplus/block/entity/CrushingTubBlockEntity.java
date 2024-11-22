package net.voidedaries.farmingplus.block.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.voidedaries.farmingplus.block.custom.CrushingTub;
import net.voidedaries.farmingplus.fluid.ModFluids;
import net.voidedaries.farmingplus.networking.ModMessages;
import net.voidedaries.farmingplus.util.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CrushingTubBlockEntity extends BlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private CrushingTub.GrapeFluidType fluidType = CrushingTub.GrapeFluidType.NONE;
    private static final int MAX_GRAPE_FLUID = 1000;
    private int grapeFluidAmount = 0;

    public static final int SLOT_1 = 0;

    public CrushingTubBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRUSHING_TUB_BLOCK_ENTITY, pos, state);
    }

    public void setFluidType(CrushingTub.GrapeFluidType type) {
        this.fluidType = type;
        markDirty();
        updateFluidLevel();
    }

    public CrushingTub.GrapeFluidType getFluidType() {
        return fluidType;
    }


    public void addGrapeFluid(int amount, CrushingTub.GrapeFluidType type) {
        if (this.fluidType == CrushingTub.GrapeFluidType.NONE) {
            setFluidType(type);
        } else if (this.fluidType != type) {
            return;
        }
        setGrapeFluidAmount(this.grapeFluidAmount + amount);

        if (this.grapeFluidAmount == 0) {
            setFluidType(CrushingTub.GrapeFluidType.NONE);
        }
    }



    public void setGrapeFluidAmount(int amount) {
        this.grapeFluidAmount = Math.min(amount, MAX_GRAPE_FLUID);

        if (this.grapeFluidAmount == 0) {
            setFluidType(CrushingTub.GrapeFluidType.NONE);
        }

        markDirty();
        updateFluidLevel();
    }


    public int getGrapeFluidAmount() {
        return this.grapeFluidAmount;
    }

    public boolean isFluidFull() {
        return grapeFluidAmount >= MAX_GRAPE_FLUID;
    }

    public ItemStack collectGrapeFluid(PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getStackInHand(hand);

        if (heldItem.getItem() == Items.BUCKET && isFluidFull()) {
            ItemStack filledBucket = switch (this.fluidType) {
                case RED -> new ItemStack(ModFluids.RED_GRAPE_FLUID_BUCKET);
                case BLUE -> new ItemStack(ModFluids.BLUE_GRAPE_FLUID_BUCKET);
                case WHITE -> new ItemStack(ModFluids.WHITE_GRAPE_FLUID_BUCKET);
                default -> ItemStack.EMPTY;
            };

            if (!filledBucket.isEmpty()) {
                heldItem.decrement(1);
                setGrapeFluidAmount(grapeFluidAmount - 1000);

                if (grapeFluidAmount == 0) {
                    setFluidType(CrushingTub.GrapeFluidType.NONE);
                }

                if (!player.getInventory().insertStack(filledBucket)) {
                    player.dropItem(filledBucket, false);
                }

                return filledBucket;
            }
        }

        return heldItem;
    }


    public void updateFluidLevel() {
        if (this.world != null) {
            int fluidLevel = calculateFluidLevel();
            BlockState currentState = this.world.getBlockState(this.pos);
            int currentFluidLevel = currentState.get(CrushingTub.FLUID_LEVEL);
            CrushingTub.GrapeFluidType currentFluidType = currentState.get(CrushingTub.FLUID_TYPE);

            if (fluidLevel == 0 && this.fluidType != CrushingTub.GrapeFluidType.NONE) {
                this.fluidType = CrushingTub.GrapeFluidType.NONE;
            }

            if (fluidLevel != currentFluidLevel || fluidType != currentFluidType) {
                this.world.setBlockState(this.pos, currentState
                        .with(CrushingTub.FLUID_LEVEL, fluidLevel)
                        .with(CrushingTub.FLUID_TYPE, fluidType), Block.NOTIFY_ALL);
            }
        }
    }


    public int calculateFluidLevel() {
        return grapeFluidAmount * 8 / MAX_GRAPE_FLUID;
    }

    private void extractFluid(CrushingTubBlockEntity entity) {
        try (Transaction transaction = Transaction.openOuter()) {
            entity.fluidStorage.extract(FluidVariant.of(ModFluids.STILL_RED_GRAPE_FLUID),
                    1000, transaction);
            transaction.commit();
        }
    }

    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 1; // 1 Bucket
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
            if (!world.isClient()) {
                PacketByteBuf data = PacketByteBufs.create();
                variant.toPacket(data);
                data.writeLong(amount);
                data.writeBlockPos(getPos());

                for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
                    ServerPlayNetworking.send(player, ModMessages.FLUID_SYNC, data);
                }
            }
        }
    };

    private boolean hasEnoughFluid(CrushingTubBlockEntity entity) {
        return entity.fluidStorage.amount >= 1000;
    }

    public ItemStack getItem() {
        return inventory.get(SLOT_1);
    }

    public ItemStack getRenderStack() {
        return this.getStack(SLOT_1);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (world != null) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("grapeFluidAmount", grapeFluidAmount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        grapeFluidAmount = nbt.getInt("grapeFluidAmount");
    }

    public void tick(World world, BlockPos pos, BlockState state, CrushingTubBlockEntity entity) {
        if (world.isClient()) {
            return;
        }

        if (hasEnoughFluid(entity)) {
            extractFluid(entity);
            markDirty(world, pos, state);
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public void setInventorySync(Consumer<DefaultedList<ItemStack>> inventory) {
        inventory.accept(this.inventory);
        if (this.world instanceof ServerWorld serverWorld) {
            serverWorld.getChunkManager().markForUpdate(getPos());
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
        markDirty();
    }


}
