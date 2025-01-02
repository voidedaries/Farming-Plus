package net.voidedaries.farmingplus.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.voidedaries.farmingplus.item.ModToolMaterials;
import net.voidedaries.farmingplus.util.ModTags;

import java.util.List;

public class ScytheItem extends MiningToolItem {

    public ScytheItem(ToolMaterial material, int attackDamage, float attackSpeed, Item.Settings settings) {
        super(attackDamage, attackSpeed, material, ModTags.Blocks.SCYTHE_MINEABLE, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        stack.damage(1, miner, (entity) -> entity.sendToolBreakStatus(miner.getActiveHand()));
        return super.postMine(stack, world, state, pos, miner);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();

        if (player == null) {
            return ActionResult.PASS;
        }

        int range = getAoERange(stack);

        for (int dx = -range; dx <= range; dx++) {
            for (int dz = -range; dz <= range; dz++) {
                BlockPos targetPos = pos.add(dx, 0, dz);
                BlockState targetState = world.getBlockState(targetPos);

                if (targetState.getBlock() instanceof CropBlock cropBlock && cropBlock.isMature(targetState)) {
                    if (!world.isClient) {
                        List<ItemStack> drops = Block.getDroppedStacks(targetState, (ServerWorld) world, targetPos, world.getBlockEntity(targetPos));
                        for (ItemStack drop : drops) {
                            Block.dropStack(world, targetPos, drop);
                        }

                        world.setBlockState(targetPos, cropBlock.withAge(0));

                        stack.damage(1, player, (p) -> p.sendToolBreakStatus(context.getHand()));
                    }
                }
            }
        }

        return ActionResult.SUCCESS;
    }

    private int getAoERange(ItemStack stack) {
        Item item = stack.getItem();

        if (item instanceof ScytheItem) {
            ToolMaterial material = ((ScytheItem) item).getMaterial();

            if (material == ModToolMaterials.WOOD) return 0; // 1x1
            if (material == ModToolMaterials.STONE) return 1; // 2x2
            if (material == ModToolMaterials.IRON) return 2; // 3x3
            if (material == ModToolMaterials.GOLD) return 3; // 5x5
            if (material == ModToolMaterials.DIAMOND) return 3; // 4x4
            if (material == ModToolMaterials.NETHERITE) return 4; // 6x6
        }

        return 0;
    }


}