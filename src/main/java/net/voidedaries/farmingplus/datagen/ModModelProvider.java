package net.voidedaries.farmingplus.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.block.ModBlocks;
import net.voidedaries.farmingplus.block.custom.CustomCherryLeaves;
import net.voidedaries.farmingplus.block.custom.CustomFarmlandBlock;
import net.voidedaries.farmingplus.fluid.ModFluids;
import net.voidedaries.farmingplus.item.ModItems;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        //region Models
        //region Moist End Farmland
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
        //endregion
        //region End Farmland
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
        //endregion

        //region Moist Netherrack Farmland
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
        //endregion
        //region Netherrack Farmland
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
        //endregion

        //region Moist Loam Farmland
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
        //endregion
        //region Loam Farmland
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
        //endregion
        //endregion
        //region Blockstates
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.SILT);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.LOAM);
        TexturedModel.CUBE_ALL.upload(ModBlocks.CHERRY_FRUIT_LEAVES, blockStateModelGenerator.modelCollector);

        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier
                .create(ModBlocks.CHERRY_FRUIT_LEAVES).coordinate(BlockStateVariantMap
                        .create(CustomCherryLeaves.AGE).register(stage -> BlockStateVariant.create()
                                .put(VariantSettings.MODEL, stage == 3 ?
                                        Registries.BLOCK.getId(ModBlocks.CHERRY_FRUIT_LEAVES) :
                                        Registries.BLOCK.getId(Blocks.CHERRY_LEAVES)))));


        for (int i = 1; i < 9; i++) {
            blockStateModelGenerator.modelCollector.accept(new Identifier(i == 8 ?
                            "farmingplus:block/cinder_composter_ready" :
                            "farmingplus:block/cinder_composter_contents" + i),
                    composter_contents(i, i == 8 ? "farmingplus:block/cinder_composter_ready" :
                            "farmingplus:block/cinder_composter_compost",
                            "farmingplus:block/cinder_composter_compost"));

        }

        for (int i = 1; i < 9; i++) {
            blockStateModelGenerator.modelCollector.accept(new Identifier(i == 8 ?
                            "farmingplus:block/void_composter_ready" :
                            "farmingplus:block/void_composter_contents" + i),
                    composter_contents(i, i == 8 ? "farmingplus:block/void_composter_ready" :
                            "farmingplus:block/void_composter_compost",
                            "farmingplus:block/void_composter_compost"));

        }

        //region Blockstate Void Composter
        blockStateModelGenerator.blockStateCollector.accept(
                MultipartBlockStateSupplier.create(ModBlocks.VOID_COMPOSTER)
                        .with(BlockStateVariant.create().put(VariantSettings.MODEL,
                                new Identifier("farmingplus:block/void_composter")))
                        .with(When.create().set(ComposterBlock.LEVEL, 1),
                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                        new Identifier("farmingplus:block/void_composter_contents1")))
                        .with(When.create().set(ComposterBlock.LEVEL, 2),
                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                        new Identifier("farmingplus:block/void_composter_contents2")))
                        .with(When.create().set(ComposterBlock.LEVEL, 3),
                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                        new Identifier("farmingplus:block/void_composter_contents3")))
                        .with(When.create().set(ComposterBlock.LEVEL, 4),
                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                        new Identifier("farmingplus:block/void_composter_contents4")))
                        .with(When.create().set(ComposterBlock.LEVEL, 5),
                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                        new Identifier("farmingplus:block/void_composter_contents5")))
                        .with(When.create().set(ComposterBlock.LEVEL, 6),
                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                        new Identifier("farmingplus:block/void_composter_contents6")))
                        .with(When.create().set(ComposterBlock.LEVEL, 7),
                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                        new Identifier("farmingplus:block/void_composter_contents7")))
                        .with(When.create().set(ComposterBlock.LEVEL, 8),
                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                        new Identifier("farmingplus:block/void_composter_ready")))
        );
        //endregion

        //region Blockstate Cinder Composter
        blockStateModelGenerator.blockStateCollector.accept(
                MultipartBlockStateSupplier.create(ModBlocks.CINDER_COMPOSTER)
                        .with(BlockStateVariant.create().put(VariantSettings.MODEL,
                                new Identifier("farmingplus:block/cinder_composter")))
                        .with(When.create().set(ComposterBlock.LEVEL, 1),
                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                        new Identifier("farmingplus:block/cinder_composter_contents1")))
                        .with(When.create().set(ComposterBlock.LEVEL, 2),
                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                        new Identifier("farmingplus:block/cinder_composter_contents2")))
                        .with(When.create().set(ComposterBlock.LEVEL, 3),
                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                        new Identifier("farmingplus:block/cinder_composter_contents3")))
                        .with(When.create().set(ComposterBlock.LEVEL, 4),
                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                        new Identifier("farmingplus:block/cinder_composter_contents4")))
                        .with(When.create().set(ComposterBlock.LEVEL, 5),
                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                        new Identifier("farmingplus:block/cinder_composter_contents5")))
                        .with(When.create().set(ComposterBlock.LEVEL, 6),
                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                        new Identifier("farmingplus:block/cinder_composter_contents6")))
                        .with(When.create().set(ComposterBlock.LEVEL, 7),
                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                        new Identifier("farmingplus:block/cinder_composter_contents7")))
                        .with(When.create().set(ComposterBlock.LEVEL, 8),
                                BlockStateVariant.create().put(VariantSettings.MODEL,
                                        new Identifier("farmingplus:block/cinder_composter_ready")))
        );
        //endregion

        //region moist end farmland
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
        //endregion

        //region moist netherrack farmland
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
        //endregion

        //region loam
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
        //endregion
        //endregion
    }


    private static Supplier<JsonElement> composter_contents(int yLevel, String insideTexture, String particleTexture) {
        return () -> {
            JsonObject root = new JsonObject();

            JsonObject textures = new JsonObject();

            JsonArray elements = new JsonArray();
            JsonObject element1 = new JsonObject();
            JsonArray from = new JsonArray();
            JsonArray to = new JsonArray();
            JsonObject faces = new JsonObject();
            JsonObject up = new JsonObject();

            textures.addProperty("particle", particleTexture);
            textures.addProperty("inside", insideTexture);

            from.add(2);
            from.add(0);
            from.add(2);

            to.add(14);
            to.add(yLevel == 8 ? 15 : yLevel * 2 + 1);
            to.add(14);


            up.addProperty("texture", "#inside");
            faces.add("up", up);

            element1.add("from", from);
            element1.add("to", to);
            element1.add("faces", faces);

            elements.add(element1);

            root.add("elements", elements);
            root.add("textures", textures);

            return root;
        };
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        //CHERRIES
        itemModelGenerator.register(ModItems.CHERRIES, Models.GENERATED);
        itemModelGenerator.register(ModItems.GOLDEN_CHERRIES, Models.GENERATED);

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