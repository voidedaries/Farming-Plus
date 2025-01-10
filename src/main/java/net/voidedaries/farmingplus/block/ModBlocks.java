package net.voidedaries.farmingplus.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.ColorCode;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.block.custom.*;

public class ModBlocks {
    public static final Block LOAM = registerBlock("loam_block",
            new LoamBlock(FabricBlockSettings.copyOf(Blocks.DIRT)));

    public static final Block LOAM_FARMLAND = registerBlock("loam_farmland",
            new CustomFarmlandBlock(FabricBlockSettings.copyOf(Blocks.FARMLAND), ModBlocks.LOAM));

    public static final Block NETHERRACK_FARMLAND = registerBlock("netherrack_farmland",
            new CustomFarmlandBlock(FabricBlockSettings.copyOf(Blocks.FARMLAND), Blocks.NETHERRACK));

    public static final Block SOUL_SAND_FARMLAND = registerBlock("soul_sand_farmland",
            new CustomFarmlandBlock(FabricBlockSettings.copyOf(Blocks.FARMLAND), Blocks.SOUL_SAND));

    public static final Block CRIMSON_NYLIUM_FARMLAND = registerBlock("crimson_nylium_farmland",
            new CustomFarmlandBlock(FabricBlockSettings.copyOf(Blocks.FARMLAND), Blocks.CRIMSON_NYLIUM));

    public static final Block WARPED_NYLIUM_FARMLAND = registerBlock("warped_nylium_farmland",
            new CustomFarmlandBlock(FabricBlockSettings.copyOf(Blocks.FARMLAND), Blocks.WARPED_NYLIUM));

    public static final Block END_FARMLAND = registerBlock("end_farmland",
            new CustomFarmlandBlock(FabricBlockSettings.copyOf(Blocks.FARMLAND), Blocks.END_STONE));

    public static final Block CHERRY_FRUIT_LEAVES = registerBlock("cherry_fruit_leaves",
            new CustomCherryLeaves(FabricBlockSettings.copyOf(Blocks.CHERRY_LEAVES)));

    public static final Block SILT = registerBlock("silt_block",
            new ColoredFallingBlock(new ColorCode(14406560), AbstractBlock.Settings.copy(Blocks.SAND)));

    public static final Block SPRUCE_CRUSHING_TUB = registerBlock("spruce_crushing_tub",
            new CrushingTub(FabricBlockSettings.copyOf(Blocks.SPRUCE_PLANKS).nonOpaque()));

    public static final Block SPRUCE_FERMENTATION_BARREL = registerBlock("spruce_fermentation_barrel",
            new FermentationBarrelBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_PLANKS)));

    public static final Block BOTTLER = registerBlock("bottler",
            new BottlerBlock(FabricBlockSettings.copyOf(Blocks.BREWING_STAND).nonOpaque()));

    public static final Block CINDER_COMPOSTER = registerBlock("cinder_composter",
            new CustomComposterBlock(FabricBlockSettings.copyOf(Blocks.COMPOSTER)));

    public static final Block VOID_COMPOSTER = registerBlock("void_composter",
            new CustomComposterBlock(FabricBlockSettings.copyOf(Blocks.COMPOSTER)));
    static {
        CustomComposterBlock.registerDefaultCompostableItems();
    }
    //GRAPE BLOCKS
    public static final Block RED_GRAPE_VINE = registerBlock("red_grape_vine",
            new GrapeBlock(FabricBlockSettings.copyOf(Blocks.SWEET_BERRY_BUSH)));
    public static final Block BLUE_GRAPE_VINE = registerBlock("blue_grape_vine",
            new GrapeBlock(FabricBlockSettings.copyOf(Blocks.SWEET_BERRY_BUSH)));
    public static final Block WHITE_GRAPE_VINE = registerBlock("white_grape_vine",
            new GrapeBlock(FabricBlockSettings.copyOf(Blocks.SWEET_BERRY_BUSH)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(FarmingPlus.MOD_ID, name), block);
    }

    @SuppressWarnings("UnusedReturnValue")
    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(FarmingPlus.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        FarmingPlus.LOGGER.info("Registering Mod Blocks for " + FarmingPlus.MOD_ID);
    }
}
