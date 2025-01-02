package net.voidedaries.farmingplus.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.block.ModBlocks;
import net.voidedaries.farmingplus.block.custom.CustomFarmlandBlock;
import net.voidedaries.farmingplus.fluid.ModFluids;
import net.voidedaries.farmingplus.item.ModItems;

import java.util.HashMap;
import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.SILT);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.LOAM);

        //loam
        var loamFarmlandBlockStateVarMap = BlockStateVariantMap.create(CustomFarmlandBlock.MOISTURE);
        for (int i : CustomFarmlandBlock.MOISTURE.getValues()) {
            if (i>=7) {
                loamFarmlandBlockStateVarMap.register(i, new BlockStateVariant().put(VariantSettings.MODEL,
                        new Identifier("farmingplus:block/loam_farmland_moist")));
            } else {
                loamFarmlandBlockStateVarMap.register(i, new BlockStateVariant().put(VariantSettings.MODEL,
                        new Identifier("farmingplus:block/loam_farmland")));
            }
        }
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(ModBlocks.LOAM_FARMLAND)
                .coordinate(loamFarmlandBlockStateVarMap));

        //netherrack farmland
        var netherrackFarmlandBlockStateVarMap = BlockStateVariantMap.create(CustomFarmlandBlock.MOISTURE);
        for (int i : CustomFarmlandBlock.MOISTURE.getValues()) {
            if (i>=7) {
                netherrackFarmlandBlockStateVarMap.register(i, new BlockStateVariant().put(VariantSettings.MODEL,
                        new Identifier("farmingplus:block/netherrack_farmland_moist")));
            } else {
                netherrackFarmlandBlockStateVarMap.register(i, new BlockStateVariant().put(VariantSettings.MODEL,
                        new Identifier("farmingplus:block/netherrack_farmland")));
            }
        }
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(ModBlocks.NETHERRACK_FARMLAND)
                .coordinate(netherrackFarmlandBlockStateVarMap));

        //end farmland
        var endFarmlandBlockStateVarMap = BlockStateVariantMap.create(CustomFarmlandBlock.MOISTURE);
        for (int i : CustomFarmlandBlock.MOISTURE.getValues()) {
            if (i>=7) {
                endFarmlandBlockStateVarMap.register(i, new BlockStateVariant().put(VariantSettings.MODEL,
                        new Identifier("farmingplus:block/end_farmland_moist")));
            } else {
                endFarmlandBlockStateVarMap.register(i, new BlockStateVariant().put(VariantSettings.MODEL,
                        new Identifier("farmingplus:block/end_farmland")));
            }
        }
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(ModBlocks.END_FARMLAND)
                .coordinate(endFarmlandBlockStateVarMap));

        blockStateModelGenerator.modelCollector.accept(
                new Identifier("farmingplus:block/loam_farmland"),
                () -> new Model(
                        Optional.of(
                                new Identifier("minecraft:block/template_farmland")
                        ),
                        Optional.empty(),
                        TextureKey.of("top"),
                        TextureKey.of("dirt")
                ).createJson(
                        new Identifier("farmingplus:block/loam_farmland"),
                        new HashMap<>() {{
                            put(TextureKey.of("top"), new Identifier("farmingplus:block/loam_farmland"));
                            put(TextureKey.of("dirt"), new Identifier("farmingplus:block/loam_block"));
                        }}
                ));

        blockStateModelGenerator.modelCollector.accept(
                new Identifier("farmingplus:block/loam_farmland_moist"),
                () -> new Model(
                        Optional.of(
                                new Identifier("minecraft:block/template_farmland")
                        ),
                        Optional.empty(),
                        TextureKey.of("top"),
                        TextureKey.of("dirt")
                ).createJson(
                        new Identifier("farmingplus:block/loam_farmland"),
                        new HashMap<>() {{
                            put(TextureKey.of("top"), new Identifier("farmingplus:block/loam_farmland_moist"));
                            put(TextureKey.of("dirt"), new Identifier("farmingplus:block/loam_block"));
                        }}
                ));

        blockStateModelGenerator.modelCollector.accept(
                new Identifier("farmingplus:block/netherrack_farmland"),
                () -> new Model(
                        Optional.of(
                                new Identifier("minecraft:block/template_farmland")
                        ),
                        Optional.empty(),
                        TextureKey.of("top"),
                        TextureKey.of("dirt")
                ).createJson(
                        new Identifier("farmingplus:block/netherrack_farmland"),
                        new HashMap<>() {{
                            put(TextureKey.of("top"), new Identifier("farmingplus:block/netherrack_farmland"));
                            put(TextureKey.of("dirt"), new Identifier("minecraft:block/netherrack"));
                        }}
                ));

        blockStateModelGenerator.modelCollector.accept(
                new Identifier("farmingplus:block/netherrack_farmland_moist"),
                () -> new Model(
                        Optional.of(
                                new Identifier("minecraft:block/template_farmland")
                        ),
                        Optional.empty(),
                        TextureKey.of("top"),
                        TextureKey.of("dirt")
                ).createJson(
                        new Identifier("farmingplus:block/netherrack_farmland"),
                        new HashMap<>() {{
                            put(TextureKey.of("top"), new Identifier("farmingplus:block/netherrack_farmland_most"));
                            put(TextureKey.of("dirt"), new Identifier("minecraft:block/netherrack"));
                        }}
                ));

        blockStateModelGenerator.modelCollector.accept(
                new Identifier("farmingplus:block/end_farmland"),
                () -> new Model(
                        Optional.of(
                                new Identifier("minecraft:block/template_farmland")
                        ),
                        Optional.empty(),
                        TextureKey.of("top"),
                        TextureKey.of("dirt")
                ).createJson(
                        new Identifier("farmingplus:block/end_farmland"),
                        new HashMap<>() {{
                            put(TextureKey.of("top"), new Identifier("farmingplus:block/end_farmland"));
                            put(TextureKey.of("dirt"), new Identifier("minecraft:block/end_stone"));
                        }}
                ));

        blockStateModelGenerator.modelCollector.accept(
                new Identifier("farmingplus:block/end_farmland_moist"),
                () -> new Model(
                        Optional.of(
                                new Identifier("minecraft:block/template_farmland")
                        ),
                        Optional.empty(),
                        TextureKey.of("top"),
                        TextureKey.of("dirt")
                ).createJson(
                        new Identifier("farmingplus:block/end_farmland"),
                        new HashMap<>() {{
                            put(TextureKey.of("top"), new Identifier("farmingplus:block/end_farmland_moist"));
                            put(TextureKey.of("dirt"), new Identifier("minecraft:block/end_stone"));
                        }}
                ));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        //GRAPES
        itemModelGenerator.register(ModItems.WHITE_GRAPES, Models.GENERATED);
        itemModelGenerator.register(ModItems.BLUE_GRAPES, Models.GENERATED);
        itemModelGenerator.register(ModItems.RED_GRAPES, Models.GENERATED);

        //WINE
        itemModelGenerator.register(ModItems.WHITE_WINE, Models.GENERATED);
        itemModelGenerator.register(ModItems.BLUE_WINE, Models.GENERATED);
        itemModelGenerator.register(ModItems.RED_WINE, Models.GENERATED);

        //FERMENTED WINE
        itemModelGenerator.register(ModFluids.WHITE_FERMENTED_FLUID_BOTTLE, Models.GENERATED);
        itemModelGenerator.register(ModFluids.BLUE_FERMENTED_FLUID_BOTTLE, Models.GENERATED);
        itemModelGenerator.register(ModFluids.RED_FERMENTED_FLUID_BOTTLE, Models.GENERATED);

        itemModelGenerator.register(ModFluids.WHITE_FERMENTED_FLUID_BUCKET, Models.GENERATED);
        itemModelGenerator.register(ModFluids.BLUE_FERMENTED_FLUID_BUCKET, Models.GENERATED);
        itemModelGenerator.register(ModFluids.RED_FERMENTED_FLUID_BUCKET, Models.GENERATED);

        //GRAPE JUICE
        itemModelGenerator.register(ModFluids.WHITE_GRAPE_FLUID_BOTTLE, Models.GENERATED);
        itemModelGenerator.register(ModFluids.BLUE_GRAPE_FLUID_BOTTLE, Models.GENERATED);
        itemModelGenerator.register(ModFluids.RED_GRAPE_FLUID_BOTTLE, Models.GENERATED);

        itemModelGenerator.register(ModFluids.WHITE_GRAPE_FLUID_BUCKET, Models.GENERATED);
        itemModelGenerator.register(ModFluids.BLUE_GRAPE_FLUID_BUCKET, Models.GENERATED);
        itemModelGenerator.register(ModFluids.RED_GRAPE_FLUID_BUCKET, Models.GENERATED);

        itemModelGenerator.register(ModItems.EMBER_SHARD, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENDER_SHARD, Models.GENERATED);

        //SCYTHES
        itemModelGenerator.register(ModItems.WOODEN_SCYTHE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.STONE_SCYTHE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.IRON_SCYTHE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.GOLDEN_SCYTHE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.DIAMOND_SCYTHE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.NETHERITE_SCYTHE, Models.HANDHELD);

        itemModelGenerator.register(ModItems.WINE_BOTTLE, Models.GENERATED);
        itemModelGenerator.register(ModItems.CORK, Models.GENERATED);
        itemModelGenerator.register(ModItems.FERTILISER, Models.GENERATED);
        itemModelGenerator.register(ModItems.SEED_BAG, Models.GENERATED);
        itemModelGenerator.register(ModItems.MANURE, Models.GENERATED);
    }

}