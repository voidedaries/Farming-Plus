package net.voidedaries.farmingplus.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.voidedaries.farmingplus.block.ModBlocks;

public class LoamBlock extends Block {

    public LoamBlock(Settings settings) {
        super(settings);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);

        if (stack.getItem() instanceof HoeItem) {
            if (world.isClient) {
                return ActionResult.SUCCESS;
            } else {
                world.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!player.isCreative()) {
                    stack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
                }
                world.setBlockState(pos, ModBlocks.LOAM_FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, 0), 11);
                return ActionResult.CONSUME;
            }
        }

        return ActionResult.PASS;
    }
}