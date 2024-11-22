package net.voidedaries.farmingplus.datagen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.world.features.SiltFeature;

public class ModFeatures {

    public static final Feature<DefaultFeatureConfig> SILT_FEATURE = new SiltFeature(DefaultFeatureConfig.CODEC);

    public static void registerFeatures() {
        Registry.register(Registries.FEATURE, new Identifier(FarmingPlus.MOD_ID, "silt_feature"), SILT_FEATURE);
    }

}
