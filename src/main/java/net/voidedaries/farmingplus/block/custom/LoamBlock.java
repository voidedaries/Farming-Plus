package net.voidedaries.farmingplus.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
        ItemStack itemStack = player.getStackInHand(hand);

        if (itemStack.getItem() == Items.WOODEN_HOE || itemStack.getItem() == Items.STONE_HOE || itemStack.getItem() == Items.IRON_HOE || itemStack.getItem() == Items.GOLDEN_HOE || itemStack.getItem() == Items.DIAMOND_HOE || itemStack.getItem() == Items.NETHERITE_HOE) {
            if (world.isClient) {
                return ActionResult.SUCCESS;
            } else {
                world.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!player.isCreative()) {
                    itemStack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
                }
                world.setBlockState(pos, ModBlocks.LOAM_FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, 0), 11);
                return ActionResult.CONSUME;
            }
        }

        return ActionResult.PASS;
    }
}
