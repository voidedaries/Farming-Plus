package net.voidedaries.farmingplus.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.voidedaries.farmingplus.world.ModPlacedFeatures;

public class ModBlockGeneration {

    public static void generateBlocks() {
        BiomeModifications.addFeature(
                context -> context.getBiomeKey().equals(BiomeKeys.RIVER) ||
                        context.getBiomeKey().equals(BiomeKeys.SWAMP) ||
                        context.getBiomeKey().equals(BiomeKeys.BEACH) ||
                        context.getBiomeKey().equals(BiomeKeys.LUSH_CAVES) ||
                        context.getBiomeKey().equals(BiomeKeys.FOREST) ||
                        context.getBiomeKey().equals(BiomeKeys.TAIGA) ||
                        context.getBiomeKey().equals(BiomeKeys.DARK_FOREST),
                GenerationStep.Feature.LOCAL_MODIFICATIONS, ModPlacedFeatures.SILT_BLOCK_PLACED_KEY);

        generateGrapeVines();
    }

    private static void generateGrapeVines() {
        BiomeModifications.addFeature(
                context -> context.getBiomeKey().equals(BiomeKeys.PLAINS) ||
                        context.getBiomeKey().equals(BiomeKeys.FOREST) ||
                        context.getBiomeKey().equals(BiomeKeys.SAVANNA),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.RED_GRAPE_VINE_PLACED_KEY);

        BiomeModifications.addFeature(
                context -> context.getBiomeKey().equals(BiomeKeys.DARK_FOREST) ||
                        context.getBiomeKey().equals(BiomeKeys.BIRCH_FOREST) ||
                        context.getBiomeKey().equals(BiomeKeys.WINDSWEPT_HILLS),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.BLUE_GRAPE_VINE_PLACED_KEY);

        BiomeModifications.addFeature(
                context -> context.getBiomeKey().equals(BiomeKeys.MEADOW) ||
                        context.getBiomeKey().equals(BiomeKeys.PLAINS) ||
                        context.getBiomeKey().equals(BiomeKeys.SNOWY_TAIGA) ||
                        context.getBiomeKey().equals(BiomeKeys.SNOWY_PLAINS),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.WHITE_GRAPE_VINE_PLACED_KEY);
    }

}
