package net.voidedaries.farmingplus.fluid.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DrinkableBottleItem extends Item {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final FlowableFluid fluid;

    public DrinkableBottleItem(FlowableFluid fluid, Settings settings) {
        super(settings);
        this.fluid = fluid;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        // Here you can define what happens when the player drinks the bottle, etc.
        ItemStack itemStack = player.getStackInHand(hand);
        player.getItemCooldownManager().set(this, 20); // Optional: Add a cooldown

        // If needed add functionality here
        //noinspection StatementWithEmptyBody
        if (!world.isClient) {
            // Optionally give status effects or custom behavior here
        }

        // Return the glass bottle back
        if (!player.getAbilities().creativeMode) {
            itemStack.decrement(1);
            player.giveItemStack(new ItemStack(Items.GLASS_BOTTLE));
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
