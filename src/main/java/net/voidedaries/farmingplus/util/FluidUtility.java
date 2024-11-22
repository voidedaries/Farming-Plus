package net.voidedaries.farmingplus.util;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.voidedaries.farmingplus.fluid.ModFluids;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class FluidUtility {

    public static ItemStack getFilledContainer(ItemStack stack, FluidVariant fluidVariant) {
        if (fluidVariant.getFluid().equals(ModFluids.STILL_RED_FERMENTED_WINE)) {
            if (stack.isOf(Items.BUCKET)) {
                return new ItemStack(ModFluids.RED_FERMENTED_FLUID_BUCKET);
            } else if (stack.isOf(Items.GLASS_BOTTLE)) {
                return new ItemStack(ModFluids.RED_FERMENTED_FLUID_BOTTLE);
            }
        }

        if (fluidVariant.getFluid().equals(ModFluids.STILL_WHITE_FERMENTED_WINE)) {
            if (stack.isOf(Items.BUCKET)) {
                return new ItemStack(ModFluids.WHITE_FERMENTED_FLUID_BUCKET);
            } else if (stack.isOf(Items.GLASS_BOTTLE)) {
                return new ItemStack(ModFluids.WHITE_FERMENTED_FLUID_BOTTLE);
            }
        }

        if (fluidVariant.getFluid().equals(ModFluids.STILL_BLUE_FERMENTED_WINE)) {
            if (stack.isOf(Items.BUCKET)) {
                return new ItemStack(ModFluids.BLUE_FERMENTED_FLUID_BUCKET);
            } else if (stack.isOf(Items.GLASS_BOTTLE)) {
                return new ItemStack(ModFluids.BLUE_FERMENTED_FLUID_BOTTLE);
            }
        }

        // Return empty stack if no valid filled container exists
        return ItemStack.EMPTY;
    }

    public static Optional<FluidVariant> getEmptyContainerType(ItemStack stack) {
        // Check if the item is a bucket
        if (stack.isOf(Items.BUCKET)) {
            return Optional.of(FluidVariant.of(Fluids.EMPTY));
        }

        // Check if the item is a bottle
        if (stack.isOf(Items.GLASS_BOTTLE)) {
            return Optional.of(FluidVariant.of(Fluids.EMPTY));
        }

        // Not a recognized container type
        return Optional.empty();
    }


    public static FluidVariant getFermentedWineFluid(FluidVariant grapeFluid) {
        if (grapeFluid.getFluid().equals(ModFluids.STILL_RED_GRAPE_FLUID)) {
            return FluidVariant.of(ModFluids.STILL_RED_FERMENTED_WINE);
        } else if (grapeFluid.getFluid().equals(ModFluids.STILL_WHITE_GRAPE_FLUID)) {
            return FluidVariant.of(ModFluids.STILL_WHITE_FERMENTED_WINE);
        } else if (grapeFluid.getFluid().equals(ModFluids.STILL_BLUE_GRAPE_FLUID)) {
            return FluidVariant.of(ModFluids.STILL_BLUE_FERMENTED_WINE);
        }
        return FluidVariant.blank(); // Return a blank variant if no match
    }

    //Returns the correct wine variant for a given bucket item
    public static FluidVariant getFermentedWineFluidFromBucket(FluidVariant grapeFluid) {
        if (grapeFluid.getFluid().equals(ModFluids.RED_GRAPE_FLUID_BUCKET)) {
            return FluidVariant.of(ModFluids.STILL_RED_FERMENTED_WINE);
        } else if (grapeFluid.getFluid().equals(ModFluids.WHITE_GRAPE_FLUID_BUCKET)) {
            return FluidVariant.of(ModFluids.STILL_WHITE_FERMENTED_WINE);
        } else if (grapeFluid.getFluid().equals(ModFluids.BLUE_GRAPE_FLUID_BUCKET)) {
            return FluidVariant.of(ModFluids.STILL_BLUE_FERMENTED_WINE);
        }
        return FluidVariant.blank(); // Return a blank variant if no match
    }

    //Returns the corresponding FluidVariant for a given bucket item
    public static Optional<FluidVariant> getFluidVariantFromBucket(ItemStack stack) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }

        if (stack.getItem() == ModFluids.RED_GRAPE_FLUID_BUCKET) {
            return Optional.of(FluidVariant.of(ModFluids.STILL_RED_GRAPE_FLUID));
        } else if (stack.getItem() == ModFluids.WHITE_GRAPE_FLUID_BUCKET) {
            return Optional.of(FluidVariant.of(ModFluids.STILL_WHITE_GRAPE_FLUID));
        } else if (stack.getItem() == ModFluids.BLUE_GRAPE_FLUID_BUCKET) {
            return Optional.of(FluidVariant.of(ModFluids.STILL_BLUE_GRAPE_FLUID));
        }

        return Optional.empty();
    }

    public static Optional<FluidVariant> getFluidVariantFromBottle(ItemStack stack) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }

        if (stack.getItem() == ModFluids.RED_GRAPE_FLUID_BOTTLE) {
            return Optional.of(FluidVariant.of(ModFluids.STILL_RED_GRAPE_FLUID));
        } else if (stack.getItem() == ModFluids.WHITE_GRAPE_FLUID_BOTTLE) {
            return Optional.of(FluidVariant.of(ModFluids.STILL_WHITE_GRAPE_FLUID));
        } else if (stack.getItem() == ModFluids.BLUE_GRAPE_FLUID_BOTTLE) {
            return Optional.of(FluidVariant.of(ModFluids.STILL_BLUE_GRAPE_FLUID));
        }

        return Optional.empty();
    }

    //Returns the empty container item for the given container item
    public static ItemStack getEmptyBucketContainer(ItemStack filledContainer) {
        if (filledContainer.getItem() == ModFluids.RED_GRAPE_FLUID_BUCKET ||
                filledContainer.getItem() == ModFluids.WHITE_GRAPE_FLUID_BUCKET ||
                filledContainer.getItem() == ModFluids.BLUE_GRAPE_FLUID_BUCKET) {
            return new ItemStack(Items.BUCKET);
        }

        return ItemStack.EMPTY;
    }

    public static ItemStack getEmptyBottleContainer(ItemStack filledContainer) {
        if (filledContainer.getItem() == ModFluids.RED_GRAPE_FLUID_BOTTLE ||
                filledContainer.getItem() == ModFluids.WHITE_GRAPE_FLUID_BOTTLE ||
                filledContainer.getItem() == ModFluids.BLUE_GRAPE_FLUID_BOTTLE) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }

        return ItemStack.EMPTY;
    }

}
