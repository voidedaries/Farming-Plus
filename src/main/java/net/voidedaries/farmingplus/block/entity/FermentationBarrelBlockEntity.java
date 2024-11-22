package net.voidedaries.farmingplus.block.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.voidedaries.farmingplus.networking.ModMessages;
import net.voidedaries.farmingplus.screen.FermentationBarrelScreenHandler;
import net.voidedaries.farmingplus.util.FluidStack;
import net.voidedaries.farmingplus.util.FluidUtility;
import net.voidedaries.farmingplus.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class FermentationBarrelBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    private static final long PRIMARY_FLUID_CAPACITY = FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 10;

    public final SingleVariantStorage<FluidVariant> primaryFluidStorage = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 10;
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
            if (world != null && !world.isClient()) {
                sendPrimaryFluidPacket();
            }
        }
    };

    private void sendPrimaryFluidPacket() {
        PacketByteBuf data = PacketByteBufs.create();
        primaryFluidStorage.variant.toPacket(data);
        data.writeLong(primaryFluidStorage.amount);
        data.writeBlockPos(getPos());

        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
            ServerPlayNetworking.send(player, ModMessages.FLUID_SYNC, data);
        }
    }

    public final SingleVariantStorage<FluidVariant> secondaryFluidStorage = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 10;
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
            if (world != null && !world.isClient()) {
                sendSecondaryFluidPacket();
            }
        }
    };

    public void sendSecondaryFluidPacket() {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeBlockPos(getPos());
        secondaryFluidStorage.variant.toPacket(data);
        data.writeLong(secondaryFluidStorage.amount);

        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
            ServerPlayNetworking.send(player, ModMessages.SECONDARY_FLUID_SYNC, data);
        }
    }

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72; //change this later

    public FermentationBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FERMENTATION_BARREL_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FermentationBarrelBlockEntity.this.progress;
                    case 1 -> FermentationBarrelBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: FermentationBarrelBlockEntity.this.progress = value; break;
                    case 1: FermentationBarrelBlockEntity.this.maxProgress = value; break;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }


    public void setPrimaryFluidLevel(FluidVariant fluidVariant, long fluidLevel) {
        this.primaryFluidStorage.variant = fluidVariant;
        this.primaryFluidStorage.amount = fluidLevel;
    }


    public void setSecondaryFluidLevel(FluidVariant fluidVariant, long fluidLevel) {
        this.secondaryFluidStorage.variant = fluidVariant;
        this.secondaryFluidStorage.amount = fluidLevel;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Fermentation Barrel");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        sendPrimaryFluidPacket();
        sendSecondaryFluidPacket();
        return new FermentationBarrelScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("fermentation_barrel.progress", progress);
        nbt.put("fermentation_barrel.primary_variant", primaryFluidStorage.variant.toNbt());
        nbt.putLong("fermentation_barrel.primary_fluidLevel", primaryFluidStorage.amount);
        nbt.put("fermentation_barrel.secondary_variant", secondaryFluidStorage.variant.toNbt());
        nbt.putLong("fermentation_barrel.secondary_fluidLevel", secondaryFluidStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("fermentation_barrel.progress");
        primaryFluidStorage.variant = FluidVariant.fromNbt((NbtCompound) nbt.get("fermentation_barrel.primary_variant"));
        primaryFluidStorage.amount = nbt.getLong("fermentation_barrel.primary_fluidLevel");
        secondaryFluidStorage.variant = FluidVariant.fromNbt((NbtCompound) nbt.get("fermentation_barrel.secondary_variant"));
        secondaryFluidStorage.amount = nbt.getLong("fermentation_barrel.secondary_fluidLevel");
    }

    private void resetProgress() {
        this.progress = 0;
    }

    public static void tick(World world, BlockPos pos, BlockState state, FermentationBarrelBlockEntity entity) {
        if(world.isClient()) {
            return;
        }

        if (entity.primaryFluidStorage.amount == 0) {
            entity.primaryFluidStorage.variant = FluidVariant.blank();
        }

        if(hasEnoughFluid(entity)) {
            entity.progress++;
            if (entity.progress >= entity.maxProgress) {
                fermentToWine(entity);
            }
        } else {
            entity.resetProgress();
            markDirty(world, pos, state);
        }

        if (hasFluidSourceInSlot(entity) && canInsertFluid(entity)) {
            transferFluidToFluidStorage(entity);
            markDirty(world, pos, state);
        }

        waitForSecondaryStorage(entity);
        transferFluidToBucket(entity);

        markDirty(world, pos, state);
    }

    private static void transferFluidToBucket(FermentationBarrelBlockEntity entity) {
        ItemStack outputstack = entity.getStack(OUTPUT_SLOT);

        Optional<FluidVariant> emptyContainerVariant = FluidUtility.getEmptyContainerType(outputstack);

        if (entity.secondaryFluidStorage.isResourceBlank() || entity.secondaryFluidStorage.getAmount() <= 0) {
            return;
        }

        if (emptyContainerVariant.isEmpty()) {
            return;
        }

        FluidVariant fermentedFluid = entity.secondaryFluidStorage.variant;
        long transferAmount = outputstack.getItem() instanceof BucketItem ?
                FluidConstants.BUCKET / 81 : FluidConstants.BOTTLE / 81;

        try (Transaction transaction = Transaction.openOuter()) {
            entity.secondaryFluidStorage.extract(fermentedFluid, transferAmount, transaction);

            ItemStack filledContainer = FluidUtility.getFilledContainer(outputstack, fermentedFluid);
            entity.setStack(OUTPUT_SLOT, filledContainer);

            transaction.commit();
        }

        entity.sendSecondaryFluidPacket();
        entity.markDirty();
    }

    private static void waitForSecondaryStorage(FermentationBarrelBlockEntity entity) {
        FluidVariant fermentedWineFluid =
                FluidUtility.getFermentedWineFluidFromBucket(entity.primaryFluidStorage.variant);

        if (fermentedWineFluid.isBlank()) {
            return;
        }

        // Check if secondary storage has the correct fermented fluid
        if (entity.secondaryFluidStorage.variant.isBlank() ||
                entity.secondaryFluidStorage.variant.equals(fermentedWineFluid)) {
                long amountToTransfer = entity.primaryFluidStorage.amount;

                try (Transaction transaction = Transaction.openOuter()) {
                    entity.secondaryFluidStorage.insert(fermentedWineFluid, amountToTransfer, transaction);
                    entity.primaryFluidStorage.amount = 0;
                    transaction.commit();
                }

                entity.sendPrimaryFluidPacket();
                entity.markDirty();
        } else {
            entity.resetProgress();
        }
    }

    private static void fermentToWine(FermentationBarrelBlockEntity entity) {
        FluidVariant grapeFluid = entity.primaryFluidStorage.variant;
        FluidVariant fermentedWineFluid = FluidUtility.getFermentedWineFluid(grapeFluid);

        if (fermentedWineFluid.isBlank() || entity.primaryFluidStorage.amount <= 0) {
            return;
        }

        long amountToTransfer = entity.primaryFluidStorage.amount;

        try (Transaction transaction = Transaction.openOuter()) {
            entity.secondaryFluidStorage.insert(fermentedWineFluid, amountToTransfer, transaction);
            entity.primaryFluidStorage.amount = 0;
            transaction.commit();
        }

        entity.sendPrimaryFluidPacket();
        entity.markDirty();

        entity.resetProgress();
    }

    private static boolean canInsertFluid(FermentationBarrelBlockEntity entity) {
        ItemStack inputstack = entity.getStack(INPUT_SLOT);
        Optional<FluidVariant> fluidVariantOpt = FluidUtility.getFluidVariantFromBucket(inputstack);

        if (fluidVariantOpt.isEmpty()) {
            fluidVariantOpt = FluidUtility.getFluidVariantFromBottle(inputstack);
        }

        if (fluidVariantOpt.isPresent()) {
            FluidVariant fluidVariant = fluidVariantOpt.get();

            return (entity.primaryFluidStorage.amount == 0 &&
                    fluidVariant.equals(entity.primaryFluidStorage.variant)) ||
                    entity.primaryFluidStorage.variant.isBlank();
        }
        return false;
    }

    private static void transferFluidToFluidStorage(FermentationBarrelBlockEntity entity) {
        ItemStack inputstack = entity.getStack(INPUT_SLOT);

        Optional<FluidVariant> fluidVariantOpt = FluidUtility.getFluidVariantFromBucket(inputstack);
        if (fluidVariantOpt.isEmpty()) {
            fluidVariantOpt = FluidUtility.getFluidVariantFromBottle(inputstack);
        }

        if (fluidVariantOpt.isPresent()) {
            FluidVariant fluidVariant = fluidVariantOpt.get();
            long transferAmount = inputstack.getItem() instanceof BucketItem ?
                    FluidConstants.BUCKET / 81 : FluidConstants.BOTTLE / 81;

            if ((entity.primaryFluidStorage.amount + transferAmount) <= PRIMARY_FLUID_CAPACITY) {
                try (Transaction transaction = Transaction.openOuter()) {
                    entity.primaryFluidStorage.insert(fluidVariant, transferAmount, transaction);

                    if (FluidUtility.getEmptyBottleContainer(inputstack).isEmpty()) {
                        entity.setStack(INPUT_SLOT, FluidUtility.getEmptyBucketContainer(inputstack));
                    } else {
                        entity.setStack(INPUT_SLOT, FluidUtility.getEmptyBottleContainer(inputstack));
                    }
                    transaction.commit();
                }
            }
        }
    }

    private static boolean hasFluidSourceInSlot(FermentationBarrelBlockEntity entity) {
        ItemStack stack = entity.getStack(INPUT_SLOT);
        return stack.isIn(ModTags.Items.GRAPE_FLUID_ITEMS);
    }

    private static boolean hasEnoughFluid(FermentationBarrelBlockEntity entity) {
        return entity.primaryFluidStorage.amount >= 333; // lowest amount you can put in the barrel, the bottle amount
    }
}