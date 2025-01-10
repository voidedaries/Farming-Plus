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
import java.util.Map;

public class ScytheItem extends MiningToolItem {

    private static final Map<ToolMaterial, List<String>> AOE_PATTERNS = Map.of(
            ModToolMaterials.WOOD, List.of(" # "),
            ModToolMaterials.STONE, List.of(" # ", "###", " # "),
            ModToolMaterials.IRON, List.of("###", "###", "###"),
            ModToolMaterials.GOLD, List.of(" ## ", "####", "####", " ## "),
            ModToolMaterials.DIAMOND, List.of(" ### ", "#####", "#####", "#####", " ### "),
            ModToolMaterials.NETHERITE, List.of(" #### ", "######", "######", "######", "######", " #### ")
    );

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
        BlockPos origin = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();

        if (player == null) {
            return ActionResult.PASS;
        }

        List<String> pattern = AOE_PATTERNS.getOrDefault(((ScytheItem) stack.getItem()).getMaterial(),
                List.of(" # "));

        int centreX = pattern.get(0).length() / 2;
        int centreZ = pattern.size() / 2;

        for (int z = 0; z < pattern.size(); z++) {
            String row = pattern.get(z);
            for (int x = 0; x < row.length(); x++) {
                if (row.charAt(x) == '#') {
                    BlockPos targetPos = origin.add(x - centreX, 0, z - centreZ);
                    BlockState targetState = world.getBlockState(targetPos);

                    if (targetState.getBlock() instanceof CropBlock cropBlock && cropBlock.isMature(targetState)) {
                        if (!world.isClient) {
                            List<ItemStack> drops = Block.getDroppedStacks(targetState, (ServerWorld) world, targetPos,
                                    world.getBlockEntity(targetPos));
                            for (ItemStack drop : drops) {
                                Block.dropStack(world, targetPos, drop);
                            }

                            world.setBlockState(targetPos, cropBlock.withAge(0));

                            stack.damage(1, player, (p) -> p.sendToolBreakStatus(context.getHand()));
                        }
                    }
                }
            }
        }

        return ActionResult.SUCCESS;
    }

}