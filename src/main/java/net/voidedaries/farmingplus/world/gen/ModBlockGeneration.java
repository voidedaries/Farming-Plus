package net.voidedaries.farmingplus.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.voidedaries.farmingplus.world.ModPlacedFeatures;

import java.util.function.Predicate;

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
    }

}
