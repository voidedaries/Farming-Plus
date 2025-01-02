package net.voidedaries.farmingplus.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.voidedaries.farmingplus.FarmingPlus;

import java.util.List;

public class ModPlacedFeatures {

    public static final RegistryKey<PlacedFeature> SILT_BLOCK_PLACED_KEY = registryKey("silt_block_placed");
    public static final RegistryKey<PlacedFeature> RED_GRAPE_VINE_PLACED_KEY = registryKey("red_grape_vine_placed");
    public static final RegistryKey<PlacedFeature> BLUE_GRAPE_VINE_PLACED_KEY = registryKey("blue_grape_vine_placed");
    public static final RegistryKey<PlacedFeature> WHITE_GRAPE_VINE_PLACED_KEY = registryKey("white_grape_vine_placed");

    public static void boostrap(Registerable<PlacedFeature> context) {
        var configuredFeatureRegistryEntryLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(context, SILT_BLOCK_PLACED_KEY, configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.SILT_BLOCK_KEY),
                ModBlockPlacement.modifiersWithCount(14,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(55), YOffset.fixed(70))));

        register(context, RED_GRAPE_VINE_PLACED_KEY,
                configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.RED_GRAPE_VINE_KEY),
                ModBlockPlacement.modifiersWithRarity(10, PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP));

        register(context, BLUE_GRAPE_VINE_PLACED_KEY,
                configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.BLUE_GRAPE_VINE_KEY),
                ModBlockPlacement.modifiersWithRarity(10, PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP));

        register(context, WHITE_GRAPE_VINE_PLACED_KEY,
                configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.WHITE_GRAPE_VINE_KEY),
                ModBlockPlacement.modifiersWithRarity(10, PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP));

    }

    public static RegistryKey<PlacedFeature> registryKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(FarmingPlus.MOD_ID, name));
    }

    @SuppressWarnings("SameParameterValue")
    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

}