package net.voidedaries.farmingplus.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import net.voidedaries.farmingplus.block.ModBlocks;
import net.voidedaries.farmingplus.item.ModItems;

@SuppressWarnings("deprecation")
public class GrapeBlock extends PlantBlock implements Fertilizable {
    public static final IntProperty AGE = Properties.AGE_3;
    private static final VoxelShape SHAPE = VoxelShapes.cuboid(0.0625, 0, 0.0625, 0.9375, 1.5, 0.9375);

    public GrapeBlock(Settings settings) {
        super(settings);
    }

    private Item getGrape() {
        if (this == ModBlocks.RED_GRAPE_VINE) {
            return ModItems.RED_GRAPES;
        } else if (this == ModBlocks.BLUE_GRAPE_VINE) {
            return ModItems.BLUE_GRAPES;
        } else if (this == ModBlocks.WHITE_GRAPE_VINE) {
            return ModItems.WHITE_GRAPES;
        } return Items.AIR;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public Item asItem() {
        if (this == ModBlocks.RED_GRAPE_VINE) {
            return ModItems.RED_GRAPES;
        } else if (this == ModBlocks.BLUE_GRAPE_VINE) {
            return ModItems.BLUE_GRAPES;
        } else if (this == ModBlocks.WHITE_GRAPE_VINE) {
            return ModItems.WHITE_GRAPES;
        }
        return Items.AIR;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(AGE) < 3;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int i = state.get(AGE);
        if (i < 3 && random.nextInt(5) == 0 && world.getBaseLightLevel(pos.up(), 0) >= 9) {
            BlockState blockState = state.with(AGE, i + 1);
            world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        boolean bl;
        int i = state.get(AGE);
        bl = i == 3;
        if (!bl && player.getStackInHand(hand).isOf(Items.BONE_MEAL)) {
            return ActionResult.PASS;
        }
        if (i > 1) {
            int j = 1 + world.random.nextInt(2);
            GrapeBlock.dropStack(world, pos, new ItemStack(getGrape(), j + (bl ? 1 : 0)));
            world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0f, 0.8f + world.random.nextFloat() * 0.4f);
            BlockState blockState = state.with(AGE, 1);
            world.setBlockState(pos, blockState, Block.NOTIFY_ALL);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, blockState));
            return ActionResult.success(world.isClient);
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return state.get(AGE) < 3;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int i = Math.min(3, state.get(AGE) + 1);
        world.setBlockState(pos, state.with(AGE, i), Block.NOTIFY_LISTENERS);
    }

    @Override
    protected MapCodec<? extends PlantBlock> getCodec() {
        return createCodec(GrapeBlock::new);
    }

}