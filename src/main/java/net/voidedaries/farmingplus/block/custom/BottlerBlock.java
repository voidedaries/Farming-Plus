package net.voidedaries.farmingplus.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.voidedaries.farmingplus.block.entity.BottlerBlockEntity;
import net.voidedaries.farmingplus.block.entity.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class BottlerBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final IntProperty BOTTLE_COUNT = IntProperty.of("bottle_count", 0, 4);

    private static final VoxelShape SHAPE = VoxelShapes.union(
            VoxelShapes.cuboid(0.4375, 0, 0.4375, 0.5625, 0.875, 0.5625),
            VoxelShapes.cuboid(0.5625, 0, 0.5625, 0.9375, 0.125, 0.9375),
            VoxelShapes.cuboid(0.5625, 0, 0.0625, 0.9375, 0.125, 0.4375),
            VoxelShapes.cuboid(0.0625, 0, 0.0625, 0.4375, 0.125, 0.4375),
            VoxelShapes.cuboid(0.0625, 0, 0.5625, 0.4375, 0.125, 0.9375)
    );

    public BottlerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(BOTTLE_COUNT, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BOTTLE_COUNT);
    }

    @Override
    @SuppressWarnings("deprecation")
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
        return new BottlerBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BottlerBlockEntity) {
                ItemScatterer.spawn(world, pos, (BottlerBlockEntity)blockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world ,pos, newState, moved);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = ((BottlerBlockEntity) world.getBlockEntity(pos));

            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.BOTTLER_BLOCK_ENTITY,
                ((world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity)));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(BottlerBlock::new);
    }
}
