package net.voidedaries.farmingplus.util;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.FarmingPlus;

public class ModTags {
    public static class Blocks {

        @SuppressWarnings("unused")
        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier(FarmingPlus.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> GRAPE_ITEMS = createTag("grape_items");
        public static final TagKey<Item> GRAPE_FLUID_ITEMS = createTag("grape_fluid_items");
        public static final TagKey<Item> FERMENTED_FLUID_BUCKETS = createTag("fermented_fluid_buckets");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(FarmingPlus.MOD_ID, name));

        }
        @SuppressWarnings("unused")
        private static TagKey<Item> createCommonTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier("minecraft", name)); // Common tag
        }
    }

    @SuppressWarnings("unused")
    public static class Fluids {
        public static final TagKey<Fluid> GRAPE_FLUIDS = createTag("grape_fluids");

        @SuppressWarnings("SameParameterValue")
        private static TagKey<Fluid> createTag(String name) {
            return TagKey.of(RegistryKeys.FLUID, new Identifier(FarmingPlus.MOD_ID, name));
        }
    }

}
