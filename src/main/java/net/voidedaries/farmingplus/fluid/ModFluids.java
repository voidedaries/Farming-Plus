package net.voidedaries.farmingplus.fluid;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.effect.ModEffects;
import net.voidedaries.farmingplus.fluid.custom.DrinkableBottleItem;
import net.voidedaries.farmingplus.fluid.custom.DrinkableBucketItem;

public class ModFluids {

    //red grape fluid
    public static FlowableFluid STILL_RED_GRAPE_FLUID = registerFluid("red_grape_fluid",
            new RedGrapeFluid.Still());
    public static FlowableFluid FLOWING_RED_GRAPE_FLUID = registerFluid("flowing_red_grape_fluid",
            new RedGrapeFluid.Flowing());
    public static Block RED_GRAPE_FLUID_BLOCK = registerFluidBlock("red_grape_fluid_block",
            STILL_RED_GRAPE_FLUID);
    public static Item RED_GRAPE_FLUID_BUCKET = registerFluidBucket("red_grape_fluid_bucket",
            STILL_RED_GRAPE_FLUID);
    public static Item RED_GRAPE_FLUID_BOTTLE = registerDrinkableFluidBottle("red_grape_fluid_bottle",
            STILL_RED_GRAPE_FLUID);

    //blue grape fluid
    public static FlowableFluid STILL_BLUE_GRAPE_FLUID = registerFluid("blue_grape_fluid",
            new BlueGrapeFluid.Still());
    public static FlowableFluid FLOWING_BLUE_GRAPE_FLUID = registerFluid("flowing_blue_grape_fluid",
            new BlueGrapeFluid.Flowing());
    public static Block BLUE_GRAPE_FLUID_BLOCK = registerFluidBlock("blue_grape_fluid_block",
            STILL_BLUE_GRAPE_FLUID);
    public static Item BLUE_GRAPE_FLUID_BUCKET = registerFluidBucket("blue_grape_fluid_bucket",
            STILL_BLUE_GRAPE_FLUID);
    public static Item BLUE_GRAPE_FLUID_BOTTLE = registerDrinkableFluidBottle("blue_grape_fluid_bottle",
            STILL_BLUE_GRAPE_FLUID);

    //white grape fluid
    public static FlowableFluid STILL_WHITE_GRAPE_FLUID = registerFluid("white_grape_fluid",
            new WhiteGrapeFluid.Still());
    public static FlowableFluid FLOWING_WHITE_GRAPE_FLUID = registerFluid("flowing_white_grape_fluid",
            new WhiteGrapeFluid.Flowing());
    public static Block WHITE_GRAPE_FLUID_BLOCK = registerFluidBlock("white_grape_fluid_block",
            STILL_WHITE_GRAPE_FLUID);
    public static Item WHITE_GRAPE_FLUID_BUCKET = registerFluidBucket("white_grape_fluid_bucket",
            STILL_WHITE_GRAPE_FLUID);
    public static Item WHITE_GRAPE_FLUID_BOTTLE = registerDrinkableFluidBottle("white_grape_fluid_bottle",
            STILL_WHITE_GRAPE_FLUID);

    //red fermented wine
    public static FlowableFluid STILL_RED_FERMENTED_WINE = registerFluid("red_fermented_wine",
            new RedFermentedWine.Still());
    public static FlowableFluid FLOWING_RED_FERMENTED_WINE = registerFluid("flowing_red_fermented_wine",
            new RedFermentedWine.Flowing());
    public static Block RED_FERMENTED_BLOCK = registerFluidBlock("red_fermented_block",
            STILL_RED_FERMENTED_WINE);
    public static Item RED_FERMENTED_FLUID_BUCKET = registerDrinkableFluidBucket("red_fermented_fluid_bucket",
            STILL_RED_FERMENTED_WINE, new StatusEffectInstance(ModEffects.INTOXICATION, 1200, 1));
    public static Item RED_FERMENTED_FLUID_BOTTLE = registerFluidBottle("red_fermented_fluid_bottle",
            STILL_RED_FERMENTED_WINE);

    //blue fermented wine
    public static FlowableFluid STILL_BLUE_FERMENTED_WINE = registerFluid("blue_fermented_wine",
            new BlueFermentedWine.Still());
    public static FlowableFluid FLOWING_BLUE_FERMENTED_WINE = registerFluid("flowing_blue_fermented_wine",
            new BlueFermentedWine.Flowing());
    public static Block BLUE_FERMENTED_BLOCK = registerFluidBlock("blue_fermented_block",
            STILL_BLUE_FERMENTED_WINE);
    public static Item BLUE_FERMENTED_FLUID_BUCKET = registerDrinkableFluidBucket("blue_fermented_fluid_bucket",
            STILL_BLUE_FERMENTED_WINE, new StatusEffectInstance(ModEffects.INTOXICATION, 1200, 1));
    public static Item BLUE_FERMENTED_FLUID_BOTTLE = registerFluidBottle("blue_fermented_fluid_bottle",
            STILL_BLUE_FERMENTED_WINE);

    //white fermented wine
    public static FlowableFluid STILL_WHITE_FERMENTED_WINE = registerFluid("white_fermented_wine",
            new WhiteFermentedWine.Still());
    public static FlowableFluid FLOWING_WHITE_FERMENTED_WINE = registerFluid("flowing_white_fermented_wine",
            new WhiteFermentedWine.Flowing());
    public static Block WHITE_FERMENTED_BLOCK = registerFluidBlock("white_fermented_block",
            STILL_WHITE_FERMENTED_WINE);
    public static Item WHITE_FERMENTED_FLUID_BUCKET = registerDrinkableFluidBucket("white_fermented_fluid_bucket",
            STILL_WHITE_FERMENTED_WINE, new StatusEffectInstance(ModEffects.INTOXICATION, 1200, 1));
    public static Item WHITE_FERMENTED_FLUID_BOTTLE = registerFluidBottle("white_fermented_fluid_bottle",
            STILL_WHITE_FERMENTED_WINE);

    private static FlowableFluid registerFluid(String name, FlowableFluid fluid) {
        return Registry.register(Registries.FLUID, new Identifier(FarmingPlus.MOD_ID, name), fluid);
    }

    private static Block registerFluidBlock(String name, FlowableFluid stillfluid) {
        return Registry.register(Registries.BLOCK, new Identifier(FarmingPlus.MOD_ID, name),
                new FluidBlock(stillfluid, FabricBlockSettings.copyOf(Blocks.WATER)));
    }

    private static Item registerFluidBucket(String name, FlowableFluid stillFluid) {
        return Registry.register(Registries.ITEM, new Identifier(FarmingPlus.MOD_ID, name),
                new BucketItem(stillFluid, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1)));
    }

    private static Item registerDrinkableFluidBucket(String name, FlowableFluid stillFluid, StatusEffectInstance effect) {
        return Registry.register(Registries.ITEM, new Identifier(FarmingPlus.MOD_ID, name),
                new DrinkableBucketItem(new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1), stillFluid, effect));
    }

    private static Item registerDrinkableFluidBottle(String name, FlowableFluid stillFluid) {
        return Registry.register(Registries.ITEM, new Identifier(FarmingPlus.MOD_ID, name),
                new DrinkableBottleItem(stillFluid, new Item.Settings().recipeRemainder(Items.GLASS_BOTTLE).maxCount(1)));
    }

    private static Item registerFluidBottle(String name, FlowableFluid stillFluid) {
        return Registry.register(Registries.ITEM, new Identifier(FarmingPlus.MOD_ID, name),
                new BucketItem(stillFluid, new Item.Settings().recipeRemainder(Items.GLASS_BOTTLE).maxCount(1)));
    }

    public static void registerModFluids() {
        FarmingPlus.LOGGER.info("Registering Mod Fluids for " + FarmingPlus.MOD_ID);
    }
}
