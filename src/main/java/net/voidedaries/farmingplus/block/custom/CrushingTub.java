package net.voidedaries.farmingplus.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.voidedaries.farmingplus.block.entity.CrushingTubBlockEntity;
import net.voidedaries.farmingplus.block.entity.ModBlockEntities;
import net.voidedaries.farmingplus.item.ModItems;
import net.voidedaries.farmingplus.networking.packet.UpdateCrushingTubS2CPacket;
import net.voidedaries.farmingplus.util.ModTags;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class CrushingTub extends BlockWithEntity implements BlockEntityProvider {
    public static final BooleanProperty HAS_ITEM = BooleanProperty.of("has_item");
    public static final IntProperty FLUID_LEVEL = IntProperty.of("fluid_level", 0, 8);
    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 8, 16);

    public enum GrapeFluidType implements StringIdentifiable {
        RED("red"),
        BLUE("blue"),
        WHITE("white"),
        NONE("none");

        private final String name;

        GrapeFluidType(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return name;
        }
    }

    public static final EnumProperty<GrapeFluidType> FLUID_TYPE = EnumProperty.of("fluid_type", GrapeFluidType.class);

    public CrushingTub(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(HAS_ITEM, false)
                .with(FLUID_LEVEL, 0)
                .with(FLUID_TYPE, GrapeFluidType.NONE));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_ITEM, FLUID_LEVEL, FLUID_TYPE);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrushingTubBlockEntity(pos, state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CrushingTubBlockEntity) {
                ItemScatterer.spawn(world, pos, (CrushingTubBlockEntity) blockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.onLandedUpon(world, state, pos, entity, fallDistance);

        if (!world.isClient && entity instanceof PlayerEntity) {
            CrushingTubBlockEntity blockEntity = (CrushingTubBlockEntity) world.getBlockEntity(pos);

            if (blockEntity != null) {
                ItemStack itemStack = blockEntity.getItem();

                if (!itemStack.isEmpty() && isGrapeItem(itemStack) && !blockEntity.isFluidFull()) {
                    GrapeFluidType currentFluidType = blockEntity.getFluidType();
                    GrapeFluidType grapeType = getGrapeTypeFromItem(itemStack);

                    // Check if the fluid type is compatible
                    if (currentFluidType == GrapeFluidType.NONE || currentFluidType == grapeType) {
                        if (grapeType != GrapeFluidType.NONE) {
                            world.playSound(null, pos, SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.BLOCKS, 1.0f, 0.5f);

                            itemStack.decrement(1);
                            blockEntity.addGrapeFluid(125, grapeType); // Add fluid

                            // Ensure the fluid type is updated in the block state if it was previously NONE
                            if (currentFluidType == GrapeFluidType.NONE) {
                                world.setBlockState(pos, state.with(FLUID_TYPE, grapeType));
                            }

                            // Handle item removal or update
                            if (itemStack.isEmpty()) {
                                blockEntity.setInventorySync(inventory -> inventory.set(0, ItemStack.EMPTY));
                                world.setBlockState(pos, state.with(HAS_ITEM, false));
                                UpdateCrushingTubS2CPacket.send((ServerPlayerEntity) entity, blockEntity);
                            } else {
                                blockEntity.setInventorySync(inventory -> inventory.set(0, itemStack.copy()));
                                UpdateCrushingTubS2CPacket.send((ServerPlayerEntity) entity, blockEntity);
                            }

                            // Update the fluid level
                            blockEntity.updateFluidLevel();
                        }
                    } else {
                        System.out.println("You can't mix different types of grapes!");
                    }
                }
            }
        }
    }

    private GrapeFluidType getGrapeTypeFromItem(ItemStack itemStack) {
        if (itemStack.getItem() == ModItems.RED_GRAPES) {
            return GrapeFluidType.RED;
        } else if (itemStack.getItem() == ModItems.BLUE_GRAPES) {
            return GrapeFluidType.BLUE;
        } else if (itemStack.getItem() == ModItems.WHITE_GRAPES) {
            return GrapeFluidType.WHITE;
        }
        return GrapeFluidType.NONE; // Default case
    }

    private boolean isGrapeItem(ItemStack itemStack) {
        return itemStack.isIn(ModTags.Items.GRAPE_ITEMS);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.PASS;
        }

        if (hand != Hand.MAIN_HAND) {
            return ActionResult.PASS;
        }

        if (!(world.getBlockEntity(pos) instanceof CrushingTubBlockEntity entity)) {
            return ActionResult.PASS;
        }

        ItemStack heldItem = player.getStackInHand(hand);
        ItemStack entityItem = entity.getItem();

        // Handle bucket interaction
        if (handleBucketInteraction(player, hand, entity)) {
            UpdateCrushingTubS2CPacket.send((ServerPlayerEntity) player, entity);
            return ActionResult.SUCCESS;
        }

        // Handle grapes interaction
        if (handleGrapeInteraction(state, world, pos, entity, heldItem, entityItem)) {
            UpdateCrushingTubS2CPacket.send((ServerPlayerEntity) player, entity);
            world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
            return ActionResult.SUCCESS;
        }

        // Handle item removal interaction
        if (handleItemRemoval(state, world, pos, player, entity, entityItem)) {
            UpdateCrushingTubS2CPacket.send((ServerPlayerEntity) player, entity);
            world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private boolean handleBucketInteraction(PlayerEntity player, Hand hand, CrushingTubBlockEntity entity) {
        ItemStack heldItem = player.getStackInHand(hand);
        if (heldItem.getItem() == Items.BUCKET && entity.isFluidFull()) {
            ItemStack resultBucket = entity.collectGrapeFluid(player, hand);
            if (!resultBucket.isEmpty()) {
                player.setStackInHand(hand, resultBucket);
            }
            return true;
        }
        return false;
    }

    private boolean handleGrapeInteraction(BlockState state, World world, BlockPos pos, CrushingTubBlockEntity entity, ItemStack heldItem, ItemStack entityItem) {
        if (!heldItem.isIn(ModTags.Items.GRAPE_ITEMS)) {
            return false;
        }

        GrapeFluidType currentFluidType = entity.getFluidType();
        GrapeFluidType newGrapeType = getGrapeTypeFromItem(heldItem);

        if (currentFluidType != GrapeFluidType.NONE && currentFluidType != newGrapeType) {
            return false;
        }

        if (entityItem.isEmpty()) {
            int amountToInsert = Math.min(heldItem.getCount(), 64);
            world.setBlockState(pos, state.with(CrushingTub.HAS_ITEM, true));
            entity.setInventorySync(inventory -> inventory.set(0, heldItem.split(amountToInsert)));
            heldItem.decrement(amountToInsert);
        } else if (ItemStack.canCombine(entityItem, heldItem)) {
            int combinedCount = entityItem.getCount() + heldItem.getCount();
            int maxCount = entityItem.getMaxCount();

            if (combinedCount <= maxCount) {
                entityItem.setCount(combinedCount);
                heldItem.setCount(0);
            } else {
                heldItem.decrement(maxCount - entityItem.getCount());
                entityItem.setCount(maxCount);
            }
            entity.setInventorySync(inventory -> inventory.set(0, entityItem.copy()));
        }
        return true;
    }


    private boolean handleItemRemoval(BlockState state, World world, BlockPos pos, PlayerEntity player, CrushingTubBlockEntity entity, ItemStack entityItem) {
        if (entityItem.isEmpty()) {
            return false;
        }

        player.giveItemStack(entityItem.copy());
        world.setBlockState(pos, state.with(CrushingTub.HAS_ITEM, false));
        entity.setInventorySync(inventory -> inventory.set(0, ItemStack.EMPTY));
        return true;
    }

    @Nullable
    @Override
    public <T extends
            BlockEntity > BlockEntityTicker < T > getTicker(World world, BlockState state, BlockEntityType < T > type) {
        return validateTicker(type, ModBlockEntities.CRUSHING_TUB_BLOCK_ENTITY,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(CrushingTub::new);
    }

}