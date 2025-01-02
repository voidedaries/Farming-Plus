package net.voidedaries.farmingplus.block.custom;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class CustomFarmlandBlock extends FarmlandBlock {

    private final Block baseBlock;

    public CustomFarmlandBlock(Settings settings, Block baseBlock) {
        super(settings);
        this.baseBlock = baseBlock;
        this.setDefaultState(this.stateManager.getDefaultState().with(FarmlandBlock.MOISTURE, 0));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP && !state.canPlaceAt(world, pos)) {
            world.scheduleBlockTick(pos, this, 1);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return !this.getDefaultState().canPlaceAt(ctx.getWorld(), ctx.getBlockPos())
                ? baseBlock.getDefaultState()
                : super.getPlacementState(ctx);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.canPlaceAt(world, pos)) {
            revertToBaseBlock(null, state, world, pos);
        }

    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int moisture = state.get(MOISTURE);

        if (baseBlock == Blocks.NETHERRACK) {
            if (world.hasRain(pos.up())) {
                if (moisture > 0) {
                    world.setBlockState(pos, state.with(MOISTURE,moisture -1), 2);
                }
            } else if (isLavaNearby(world, pos)) {
                if (moisture < 7) {
                    world.setBlockState(pos, state.with(MOISTURE, 7), 2);
                }
            } else {
                if (moisture > 0) {
                    world.setBlockState(pos, state.with(MOISTURE, moisture - 1), 2);
                } else if (!hasCrop(world, pos)) {
                    revertToBaseBlock(null, state, world, pos);
                }
            }
            return;
        }

        if (!isWaterNearby(world, pos) && !world.hasRain(pos.up())) {
            if (moisture > 0) {
                world.setBlockState(pos, state.with(MOISTURE, moisture - 1), 2);
            } else if (!hasCrop(world, pos)) {
                revertToBaseBlock(null, state, world, pos);
            }
        } else if (moisture < 7) {
            world.setBlockState(pos, state.with(MOISTURE, 7), 2);
        }

        if (hasCrop(world, pos)) {
            BlockState cropState = world.getBlockState(pos.up());

            if (cropState.getBlock() instanceof CropBlock crop) {

                if (random.nextInt(3) == 0) {
                    int age = cropState.get(CropBlock.AGE);
                    if (age < crop.getMaxAge()) {
                        world.setBlockState(pos.up(), cropState.with(CropBlock.AGE, age + 1), 2);
                    }
                }

            }
        }
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!world.isClient && world.random.nextFloat() < fallDistance - 0.5F && entity instanceof LivingEntity
                && (entity instanceof PlayerEntity || world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
                && entity.getWidth() * entity.getWidth() * entity.getHeight() > 0.512F) {
            revertToBaseBlock(entity, state, world, pos);
        }
    }

    public void revertToBaseBlock(@Nullable Entity entity, BlockState state, World world, BlockPos pos) {
        BlockState blockState = pushEntitiesUpBeforeBlockChange(state, baseBlock.getDefaultState(), world, pos);
        world.setBlockState(pos, blockState);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(entity, blockState));
    }

    private static boolean hasCrop(BlockView world, BlockPos pos) {
        return world.getBlockState(pos.up()).isIn(BlockTags.MAINTAINS_FARMLAND);
    }

    private static boolean isWaterNearby(WorldView world, BlockPos pos) {
        Iterator<BlockPos> var2 = BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 1, 4)).iterator();
        BlockPos blockPos;
        do {
            if (!var2.hasNext()) {
                return false;
            }
            blockPos = var2.next();
        } while(!world.getFluidState(blockPos).isIn(FluidTags.WATER));
        return true;
    }

    private static boolean isLavaNearby(WorldView world, BlockPos pos) {
        Iterator<BlockPos> var2 = BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 1, 4)).iterator();
        BlockPos blockPos;
        do {
            if (!var2.hasNext()) {
                return false;
            }
            blockPos = var2.next();
        } while(!world.getFluidState(blockPos).isIn(FluidTags.LAVA));
        return true;
    }

}