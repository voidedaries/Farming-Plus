package net.voidedaries.farmingplus.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.voidedaries.farmingplus.block.ModBlocks;
import net.voidedaries.farmingplus.item.ModItems;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.SILT);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.LOAM);

        blockStateModelGenerator.registerFlowerPotPlant(ModBlocks.BLUEBELL, ModBlocks.POTTED_BLUEBELL, BlockStateModelGenerator.TintType.NOT_TINTED);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.RED_GRAPES, Models.GENERATED);
        itemModelGenerator.register(ModItems.WHITE_GRAPES, Models.GENERATED);
        itemModelGenerator.register(ModItems.BLUE_GRAPES, Models.GENERATED);
        itemModelGenerator.register(ModItems.WINE_BOTTLE, Models.GENERATED);
        itemModelGenerator.register(ModItems.RED_WINE, Models.GENERATED);
        itemModelGenerator.register(ModItems.BLUE_WINE, Models.GENERATED);
        itemModelGenerator.register(ModItems.WHITE_WINE, Models.GENERATED);
        itemModelGenerator.register(ModItems.MANURE, Models.GENERATED);
        itemModelGenerator.register(ModItems.FERTILISER, Models.GENERATED);
    }

}