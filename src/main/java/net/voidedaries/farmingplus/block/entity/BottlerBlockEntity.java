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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.voidedaries.farmingplus.block.custom.BottlerBlock;
import net.voidedaries.farmingplus.fluid.ModFluids;
import net.voidedaries.farmingplus.item.ModItems;
import net.voidedaries.farmingplus.networking.ModMessages;
import net.voidedaries.farmingplus.screen.BottlerScreenHandler;
import net.voidedaries.farmingplus.util.FluidStack;
import net.voidedaries.farmingplus.util.ModTags;
import org.jetbrains.annotations.Nullable;


public class BottlerBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);

    private static final int CORK_SLOT = 0;
    private static final int BUCKET_SLOT = 1;
    private static final int BOTTLE_SLOT_1 = 2;
    private static final int BOTTLE_SLOT_2 = 3;
    private static final int BOTTLE_SLOT_3 = 4;
    private static final int BOTTLE_SLOT_4 = 5;

    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 10; //10 Buckets worth
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
            if (!world.isClient()) {
                sendFluidPacket();
            }
        }

    };

    private void sendFluidPacket() {
        PacketByteBuf data = PacketByteBufs.create();
        fluidStorage.variant.toPacket(data);
        data.writeLong(fluidStorage.amount);
        data.writeBlockPos(getPos());

        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
            ServerPlayNetworking.send(player, ModMessages.BOTTLER_FLUID_SYNC, data);
        }
    }

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 400;

    public int countBottles() {
        int bottleCount = 0;
        for (int i = BOTTLE_SLOT_1; i <= BOTTLE_SLOT_4; i++) {
            if (!this.getStack(i).isEmpty()) {
                bottleCount++;
            }
        }
        return bottleCount;
    }

    public void updateBlockState() {
        int bottleCount = countBottles();  // Count the number of bottles
        if (this.world != null && !this.world.isClient) {
            BlockState currentState = this.world.getBlockState(this.pos);
            this.world.setBlockState(this.pos, currentState.with(BottlerBlock.BOTTLE_COUNT, bottleCount), 3);
            this.markDirty();
        }
    }


    public BottlerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BOTTLER_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BottlerBlockEntity.this.progress;
                    case 1 -> BottlerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> BottlerBlockEntity.this.progress = value;
                    case 1 -> BottlerBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    public void setFluidLevel(FluidVariant fluidVariant, long fluidLevel) {
        this.fluidStorage.variant = fluidVariant;
        this.fluidStorage.amount = fluidLevel;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Bottler");
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("bottler.progress", progress);
        nbt.putInt("bottler.bottle_count", countBottles());
        nbt.put("bottler.variant", fluidStorage.variant.toNbt());
        nbt.putLong("bottler.fluid", fluidStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("bottler.progress");
        fluidStorage.variant = FluidVariant.fromNbt((NbtCompound) nbt.get("bottler.variant"));
        fluidStorage.amount = nbt.getLong("bottler.fluid");
        updateBlockState();
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        sendFluidPacket();
        return new BottlerScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public void tick(World world, BlockPos pos, BlockState state, BottlerBlockEntity entity) {
        int bottleCount = entity.countBottles();

        if (world.isClient()) {
            return;
        }

        if (state.get(BottlerBlock.BOTTLE_COUNT) != bottleCount) {
            entity.updateBlockState();
        }

        if (canCraftWine(entity) && hasEnoughFluid(entity)) {
            if (canCraftWine(entity)) {
                entity.increaseCraftProgress();
                markDirty();

                if (entity.hasCraftFinished()) {
                    craftWine(entity);
                    extractFluid(entity);
                    entity.resetProgress();
                }
            } else {
                entity.resetProgress();
                markDirty();
            }
        }

        if (hasFluidSourceInSlot(entity) && !isFluidStorageFull()) {
            transferFluidToFluidStorage(entity);
        }

    }

    private boolean isFluidStorageFull() {
        return fluidStorage.getAmount() >= fluidStorage.getCapacity();
    }

    public static void extractFluid(BottlerBlockEntity entity) {
        FluidVariant storedFluid = entity.fluidStorage.getResource();
        long fluidAmountToExtract = FluidStack.convertDropletsToMb(FluidConstants.BUCKET);

        try (Transaction transaction = Transaction.openOuter()) {
            if (storedFluid.equals(FluidVariant.of(ModFluids.STILL_RED_FERMENTED_WINE))) {
                entity.fluidStorage.extract(storedFluid, fluidAmountToExtract, transaction);
            } else if (storedFluid.equals(FluidVariant.of(ModFluids.STILL_WHITE_FERMENTED_WINE))) {
                entity.fluidStorage.extract(storedFluid, fluidAmountToExtract, transaction);
            } else if (storedFluid.equals(FluidVariant.of(ModFluids.STILL_BLUE_FERMENTED_WINE))) {
                entity.fluidStorage.extract(storedFluid, fluidAmountToExtract, transaction);
            }
            transaction.commit();
        }
    }

    private void transferFluidToFluidStorage(BottlerBlockEntity entity) {
        ItemStack bucketStack = entity.getStack(BUCKET_SLOT);
        FluidVariant fluidVariant = FluidVariant.blank();

        if (bucketStack.getItem() == ModFluids.RED_FERMENTED_FLUID_BUCKET) {
            fluidVariant = FluidVariant.of(ModFluids.STILL_RED_FERMENTED_WINE);
        } else if (bucketStack.getItem() == ModFluids.WHITE_FERMENTED_FLUID_BUCKET) {
            fluidVariant = FluidVariant.of(ModFluids.STILL_WHITE_FERMENTED_WINE);
        } else if (bucketStack.getItem() == ModFluids.BLUE_FERMENTED_FLUID_BUCKET) {
            fluidVariant = FluidVariant.of(ModFluids.STILL_BLUE_FERMENTED_WINE);
        }

        if (!fluidVariant.isBlank()) {
            long fluidAmountToTransfer = FluidStack.convertDropletsToMb(FluidConstants.BUCKET);

            try (Transaction transaction = Transaction.openOuter()) {
                entity.fluidStorage.insert(fluidVariant, fluidAmountToTransfer, transaction);
                entity.setStack(BUCKET_SLOT, new ItemStack(Items.BUCKET));
                transaction.commit();
            }
        }
    }

    private boolean hasFluidSourceInSlot(BottlerBlockEntity entity) {
        ItemStack stack = entity.getStack(BUCKET_SLOT);
        return stack.isIn(ModTags.Items.FERMENTED_FLUID_BUCKETS);
    }

    private static boolean hasEnoughFluid(BottlerBlockEntity entity) {
        return entity.fluidStorage.amount >= 1000;
    }

    private boolean canCraftWine(BottlerBlockEntity entity) {
        ItemStack corkStack = entity.getStack(CORK_SLOT);
        if (corkStack.isEmpty() || corkStack.getCount() < getNumberOfBottles(entity)) return false;

        for (int i = BOTTLE_SLOT_1; i <= BOTTLE_SLOT_4; i++) {
            if (!entity.getStack(i).isOf(ModItems.WINE_BOTTLE)) {
                return false;
            }
        }

        return true;
    }

    private int getNumberOfBottles(BottlerBlockEntity entity) {
        int bottleCount = 0;
        for (int i = BOTTLE_SLOT_1; i <= BOTTLE_SLOT_4; i++) {
            if (entity.getStack(i).isOf(ModItems.WINE_BOTTLE)) {
                bottleCount++;
            }
        }
        return bottleCount;
    }

    private void craftWine(BottlerBlockEntity entity) {
        int bottleCount = getNumberOfBottles(entity);
        Item wineType = getWineTypeFromFluid(entity.fluidStorage.variant);

        if (wineType == null) {
            return;
        }

        ItemStack corkStack = entity.getStack(CORK_SLOT);
        corkStack.decrement(bottleCount);

        for (int i = BOTTLE_SLOT_1; i <= BOTTLE_SLOT_4; i++) {
            ItemStack bottleStack = entity.getStack(i);
            if (bottleStack.isOf(ModItems.WINE_BOTTLE)) {
                entity.setStack(i, new ItemStack(wineType));
            }
        }
    }

    private Item getWineTypeFromFluid(FluidVariant fluidVariant) {
        if (fluidVariant.isOf(ModFluids.STILL_RED_FERMENTED_WINE)) {
            return ModItems.RED_WINE;
        } else if (fluidVariant.isOf(ModFluids.STILL_WHITE_FERMENTED_WINE)) {
            return ModItems.WHITE_WINE;
        } else if (fluidVariant.isOf(ModFluids.STILL_BLUE_FERMENTED_WINE)) {
            return ModItems.BLUE_WINE;
        }

        return null;
    }

    private void increaseCraftProgress() {
        progress++;
    }

    private boolean hasCraftFinished() {
        return progress >= maxProgress;
    }

    private void resetProgress() {
        progress = 0;
    }

}