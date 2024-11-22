package net.voidedaries.farmingplus.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.voidedaries.farmingplus.effect.ModEffects;
import net.voidedaries.farmingplus.item.ModItems;
import net.voidedaries.farmingplus.mixininterface.StatusSavingPlayer;

public class WineItem extends Item {
    public WineItem(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 24;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        if (player.getAbilities().creativeMode) {
            return TypedActionResult.pass(itemStack);
        }

        player.setCurrentHand(hand);

        return TypedActionResult.consume(itemStack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            StatusSavingPlayer statusPlayer = (StatusSavingPlayer) player;

            Integer wineCount = statusPlayer.farmingplus$getWineCount();
            if (wineCount == null) {
                wineCount = 0;
            }

            wineCount++;
            statusPlayer.farmingplus$setWineCount(wineCount);

            if (!world.isClient) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 0));
            }

            if (!world.isClient) {
                float intoxicationChance = wineCount * 0.25f;
                if (world.random.nextFloat() < intoxicationChance) {
                    player.addStatusEffect(new StatusEffectInstance(ModEffects.INTOXICATION, 600, 1));
                }
            }

            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }

            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_HONEY_BOTTLE_DRINK,
                    user.getSoundCategory(), 1.0F, 1.0F);

            if (stack.isEmpty()) {
                return new ItemStack(ModItems.WINE_BOTTLE);
            }

            if (!player.getInventory().insertStack(new ItemStack(ModItems.WINE_BOTTLE))) {
                player.dropItem(new ItemStack(ModItems.WINE_BOTTLE), false);
            }

        }

        return stack;
    }

}
