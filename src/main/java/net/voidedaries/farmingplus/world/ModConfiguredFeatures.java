package net.voidedaries.farmingplus.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.block.ModBlocks;
import net.voidedaries.farmingplus.datagen.ModFeatures;

public class ModConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> SILT_BLOCK_KEY = registryKey("silt_block");
    public static final RegistryKey<ConfiguredFeature<?, ?>> RED_GRAPE_VINE_KEY = registryKey("red_grape_vine");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BLUE_GRAPE_VINE_KEY = registryKey("blue_grape_vine");
    public static final RegistryKey<ConfiguredFeature<?, ?>> WHITE_GRAPE_VINE_KEY = registryKey("white_grape_vine");


    public static void boostrap(Registerable<ConfiguredFeature<?, ?>> context) {

        register(context, SILT_BLOCK_KEY, ModFeatures.SILT_FEATURE, new DefaultFeatureConfig());

        context.register(RED_GRAPE_VINE_KEY, new ConfiguredFeature<>(
                Feature.FLOWER,
                new RandomPatchFeatureConfig(4, 7, 3, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                        new SimpleBlockFeatureConfig(BlockStateProvider.of(ModBlocks.RED_GRAPE_VINE.getDefaultState()
                                .with(Properties.AGE_3, 3)))))
        ));

        context.register(BLUE_GRAPE_VINE_KEY, new ConfiguredFeature<>(
                Feature.FLOWER,
                new RandomPatchFeatureConfig(4, 7, 3, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                        new SimpleBlockFeatureConfig(BlockStateProvider.of(ModBlocks.BLUE_GRAPE_VINE.getDefaultState()
                                .with(Properties.AGE_3, 3)))))
        ));

        context.register(WHITE_GRAPE_VINE_KEY, new ConfiguredFeature<>(
                Feature.FLOWER,
                new RandomPatchFeatureConfig(4, 7, 3, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                        new SimpleBlockFeatureConfig(BlockStateProvider.of(ModBlocks.WHITE_GRAPE_VINE.getDefaultState()
                                .with(Properties.AGE_3, 3)))))
        ));


    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registryKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(FarmingPlus.MOD_ID, name));
    }

    @SuppressWarnings("SameParameterValue")
    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

}
