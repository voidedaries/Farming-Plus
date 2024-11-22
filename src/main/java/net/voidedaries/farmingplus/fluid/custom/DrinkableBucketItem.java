package net.voidedaries.farmingplus.fluid.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class DrinkableBucketItem extends BucketItem {

    private final StatusEffectInstance effect;
    private final Fluid fluid;

    public DrinkableBucketItem(Settings settings, Fluid fluid, StatusEffectInstance effect) {
        super(fluid, settings);
        this.effect = effect;
        this.fluid = fluid;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        // Use raycasting to determine if the player is looking at a block
        BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = hitResult.getBlockPos().offset(hitResult.getSide());

            // Check if the block can be replaced by the fluid
            if (canPlaceFluid(world, blockPos)) {
                if (!world.isClient) {
                    world.setBlockState(blockPos, fluid.getDefaultState().getBlockState(), 11);

                    // Replace the bucket with an empty one
                    if (!user.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            return TypedActionResult.success(new ItemStack(Items.BUCKET), world.isClient());
                        }
                        user.getInventory().insertStack(new ItemStack(Items.BUCKET));
                    }
                }
                return TypedActionResult.success(itemStack);
            }
        }
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            if (this.effect != null && !world.isClient) {
                player.addStatusEffect(new StatusEffectInstance(this.effect));
            }

            player.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.0F, 1.0F);

            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);

                if (stack.isEmpty()) {
                    return new ItemStack(Items.BUCKET);
                } else {
                    player.getInventory().insertStack(new ItemStack(Items.BUCKET));
                }
            }

            player.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        return stack;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos;
        PlayerEntity player = context.getPlayer();
        ItemStack itemStack = context.getStack();
        //noinspection DataFlowIssue I might need to deal with this
        BlockHitResult hitResult = raycast(world, player, RaycastContext.FluidHandling.SOURCE_ONLY);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            blockPos = hitResult.getBlockPos().offset(hitResult.getSide());

            if (canPlaceFluid(world, blockPos)) {
                if (!world.isClient) {
                    BlockState fluidBlockState = this.fluid.getDefaultState().getBlockState();
                    world.setBlockState(blockPos, fluidBlockState, 11);
                }

                if (!player.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                    player.getInventory().insertStack(new ItemStack(Items.BUCKET));
                }
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    private boolean canPlaceFluid(World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        FluidState fluidState = world.getFluidState(pos);

        return (blockState.isAir() || blockState.isReplaceable() || blockState.getBlock() instanceof FluidFillable) && fluidState.isEmpty();
    }

    protected static BlockHitResult raycast(World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling) {
        float pitch = player.getPitch();
        float yaw = player.getYaw();
        Vec3d start = player.getCameraPosVec(1.0F);
        float f = MathHelper.cos(-yaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float g = MathHelper.sin(-yaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float h = -MathHelper.cos(-pitch * ((float) Math.PI / 180F));
        float i = MathHelper.sin(-pitch * ((float) Math.PI / 180F));
        float j = g * h;
        float k = f * h;
        double reachDistance = 5.0D;
        Vec3d end = start.add((double) j * reachDistance, (double) i * reachDistance, (double) k * reachDistance);
        return world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.OUTLINE, fluidHandling, player));
    }
}