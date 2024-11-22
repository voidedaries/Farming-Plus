package net.voidedaries.farmingplus.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.voidedaries.farmingplus.block.ModBlocks;

import java.util.Random;

public class SiltFeature extends Feature<DefaultFeatureConfig> {
    public SiltFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = new Random(context.getRandom().nextLong());

        int siltVeinCount = 8;
        int veinSize = 4;
        double spreadFactor = 2.8; //higher values is less spread
        boolean placedAny = false;

        for (int i = 0; i < siltVeinCount; i++) {
            BlockPos veinPos = origin.add(random.nextInt(16) - 8, random.nextInt(4) - 2,
                    random.nextInt(16) - 8);


            for (int j = 0; j < veinSize; j++) {
                BlockPos targetPos = veinPos.add(random.nextInt(4) - 2, random.nextInt(2) - 1,
                        random.nextInt(4) - 2);
                BlockState targetState = world.getBlockState(targetPos);
                BlockState belowState = world.getBlockState(targetPos.down());

                if (targetState.isOf(Blocks.SAND) && belowState.isSolid()) {
                    world.setBlockState(targetPos, ModBlocks.SILT.getDefaultState(), 3);
                    placedAny = true;

                    // Additional clustering logic based on the blobbyFactor
                    if (random.nextDouble() < spreadFactor) {
                        int extraSilt = random.nextInt(10) + 4;  // Number of extra blocks to add nearby
                        for (int k = 0; k < extraSilt; k++) {
                            BlockPos nearbyPos = targetPos.add(random.nextInt(3) - 1, random.nextInt(2) - 1, random.nextInt(3) - 1);
                            BlockState nearbyState = world.getBlockState(nearbyPos);
                            BlockState nearbyBelowState = world.getBlockState(nearbyPos.down());

                            if (nearbyState.isOf(Blocks.SAND) && nearbyBelowState.isSolid()) {
                                world.setBlockState(nearbyPos, ModBlocks.SILT.getDefaultState(), 3);
                            }
                        }
                    }
                }
            }
        }
            return placedAny;
    }

    //seed  4350889067989219121
}
