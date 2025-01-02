package net.voidedaries.farmingplus.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.block.ModBlocks;
import net.voidedaries.farmingplus.fluid.ModFluids;

@SuppressWarnings("unused")
public class ModItemGroups {
    public static final ItemGroup FARMINGPLUS = Registry.register(Registries.ITEM_GROUP,
            new Identifier(FarmingPlus.MOD_ID, "farming_plus" ),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.farming_plus"))
                    .icon(() -> new ItemStack(ModItems.FERTILISER)).entries((displayContext, entries) -> {

                        entries.add(ModItems.MANURE);
                        entries.add(ModItems.SEED_BAG);
                        entries.add(ModItems.FERTILISER);
                        entries.add(ModBlocks.SILT);
                        entries.add(ModBlocks.LOAM);

                        entries.add(ModBlocks.LOAM_FARMLAND);
                        entries.add(ModBlocks.NETHERRACK_FARMLAND);
                        entries.add(ModBlocks.END_FARMLAND);

                        entries.add(ModBlocks.SPRUCE_CRUSHING_TUB);
                        entries.add(ModBlocks.SPRUCE_FERMENTATION_BARREL);
                        entries.add(ModBlocks.BOTTLER);

                        entries.add(ModBlocks.CINDER_COMPOSTER);
                        entries.add(ModBlocks.VOID_COMPOSTER);

                        entries.add(ModItems.CORK);
                        entries.add(ModItems.WINE_BOTTLE);

                        entries.add(ModItems.EMBER_SHARD);
                        entries.add(ModItems.ENDER_SHARD);

                        //SCYTHES
                        entries.add(ModItems.WOODEN_SCYTHE);
                        entries.add(ModItems.STONE_SCYTHE);
                        entries.add(ModItems.IRON_SCYTHE);
                        entries.add(ModItems.GOLDEN_SCYTHE);
                        entries.add(ModItems.DIAMOND_SCYTHE);
                        entries.add(ModItems.NETHERITE_SCYTHE);

                        //WINE
                        entries.add(ModItems.BLUE_WINE);
                        entries.add(ModItems.RED_WINE);
                        entries.add(ModItems.WHITE_WINE);

                        //GRAPES
                        entries.add(ModItems.BLUE_GRAPES);
                        entries.add(ModItems.RED_GRAPES);
                        entries.add(ModItems.WHITE_GRAPES);

                        //GRAPE FLUIDS
                        entries.add(ModFluids.BLUE_GRAPE_FLUID_BUCKET);
                        entries.add(ModFluids.RED_GRAPE_FLUID_BUCKET);
                        entries.add(ModFluids.WHITE_GRAPE_FLUID_BUCKET);
                        entries.add(ModFluids.BLUE_GRAPE_FLUID_BOTTLE);
                        entries.add(ModFluids.RED_GRAPE_FLUID_BOTTLE);
                        entries.add(ModFluids.WHITE_GRAPE_FLUID_BOTTLE);

                        //FERMENTED FLUIDS
                        entries.add(ModFluids.BLUE_FERMENTED_FLUID_BUCKET);
                        entries.add(ModFluids.RED_FERMENTED_FLUID_BUCKET);
                        entries.add(ModFluids.WHITE_FERMENTED_FLUID_BUCKET);
                        entries.add(ModFluids.BLUE_FERMENTED_FLUID_BOTTLE);
                        entries.add(ModFluids.RED_FERMENTED_FLUID_BOTTLE);
                        entries.add(ModFluids.WHITE_FERMENTED_FLUID_BOTTLE);


                    }).build());

    public static void registerItemGroups() {
        FarmingPlus.LOGGER.info("Registering Item Groups for "+ FarmingPlus.MOD_ID);
    }
}
