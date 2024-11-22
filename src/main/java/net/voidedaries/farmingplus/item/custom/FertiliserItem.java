package net.voidedaries.farmingplus.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FertiliserItem extends BoneMealItem {

    public FertiliserItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!world.isClient) {
            if (applyFertiliser(context)) {
                context.getStack().decrement(1);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    private boolean applyFertiliser(ItemUsageContext context) {
        return applyFertiliserEffects(context.getWorld(), context.getBlockPos());
    }

    private boolean applyFertiliserEffects(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof CropBlock cropBlock) {
            int currentAge = cropBlock.getAge(state);
            int maxAge = cropBlock.getMaxAge();

            if (currentAge < maxAge) {
                //20%: Crop age = max age.
                //72%: Crop age = min(currentAge + 3, maxAge).
                //18%: Crop age = min(currentAge + 2, maxAge).
                int newAge;

                if (Math.random() < 0.2) {
                    newAge = maxAge;
                } else {
                    newAge = Math.min(currentAge + 3, maxAge);

                    if (Math.random() < 0.8) {
                        newAge = Math.min(newAge + 2, maxAge);
                    }
                }

                world.setBlockState(pos, cropBlock.withAge(newAge), 2);
                spawnFertiliserParticles(world, pos);
                return true;
            }
        } else {
            return useFertiliserOnOtherBlocks(world, pos, state);
        }

        return false;
    }

    private boolean useFertiliserOnOtherBlocks(World ignoredWorld, BlockPos ignoredPos, BlockState state) {
        Block block = state.getBlock();

        return false;
    }

    private void spawnFertiliserParticles(World world, BlockPos pos) {
        if (world instanceof ServerWorld) {
            ((ServerWorld) world).spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    10, 0.5, 0.5, 0.5, 0.5);
        }
    }

}
