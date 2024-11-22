package net.voidedaries.farmingplus.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.item.Item;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.block.custom.*;

public class ModBlocks {
    public static final Block LOAM = registerBlock("loam_block",
            new LoamBlock(FabricBlockSettings.copyOf(Blocks.DIRT)));

    public static final Block LOAM_FARMLAND = registerBlock("loam_farmland",
            new LoamFarmlandBlock(FabricBlockSettings.copyOf(Blocks.FARMLAND)));

    public static final Block SILT = registerBlock("silt_block",
            new FallingBlock(FabricBlockSettings.copyOf(Blocks.SAND)));

    public static final Block SPRUCE_CRUSHING_TUB = registerBlock("spruce_crushing_tub",
            new CrushingTub(FabricBlockSettings.copyOf(Blocks.SPRUCE_PLANKS).nonOpaque()));

    public static final Block SPRUCE_FERMENTATION_BARREL = registerBlock("spruce_fermentation_barrel",
            new FermentationBarrelBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_PLANKS)));

    public static final Block BOTTLER = registerBlock("bottler",
            new BottlerBlock(FabricBlockSettings.copyOf(Blocks.BREWING_STAND).nonOpaque()));

    // FLOWERS
    public static final Block BLUEBELL = registerBlock("bluebell",
            new FlowerBlock(StatusEffects.ABSORPTION, 0,
                    FabricBlockSettings.copyOf(Blocks.ALLIUM).nonOpaque().noCollision()));

    public static final Block POTTED_BLUEBELL = registerBlock("potted_bluebell",
            new FlowerPotBlock(BLUEBELL, FabricBlockSettings.copyOf(Blocks.POTTED_ALLIUM).nonOpaque()));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(FarmingPlus.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(FarmingPlus.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        FarmingPlus.LOGGER.info("Registering Mod Blocks for " + FarmingPlus.MOD_ID);
    }
}
