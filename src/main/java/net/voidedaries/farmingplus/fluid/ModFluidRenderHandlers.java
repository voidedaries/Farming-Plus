package net.voidedaries.farmingplus.fluid;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.FarmingPlus;

public class ModFluidRenderHandlers {
    public static void registerFluidRenders() {
        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_RED_GRAPE_FLUID, ModFluids.FLOWING_RED_GRAPE_FLUID,
                new SimpleFluidRenderHandler(
                        new Identifier("minecraft:block/water_still"),
                        new Identifier("minecraft:block/water_flow"),
                        0xAA2B35
                ));

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_BLUE_GRAPE_FLUID, ModFluids.FLOWING_BLUE_GRAPE_FLUID,
                new SimpleFluidRenderHandler(
                        new Identifier("minecraft:block/water_still"),
                        new Identifier("minecraft:block/water_flow"),
                        0x3E77A3
                ));

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_WHITE_GRAPE_FLUID, ModFluids.FLOWING_WHITE_GRAPE_FLUID,
                new SimpleFluidRenderHandler(
                        new Identifier("minecraft:block/water_still"),
                        new Identifier("minecraft:block/water_flow"),
                        0xC9EFBF
                ));

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_RED_FERMENTED_WINE, ModFluids.FLOWING_RED_FERMENTED_WINE,
                new SimpleFluidRenderHandler(
                        new Identifier("minecraft:block/water_still"),
                        new Identifier("minecraft:block/water_flow"),
                        0x8B0000
                ));

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_BLUE_FERMENTED_WINE, ModFluids.FLOWING_BLUE_FERMENTED_WINE,
                new SimpleFluidRenderHandler(
                        new Identifier("minecraft:block/water_still"),
                        new Identifier("minecraft:block/water_flow"),
                        0x1831A0
                ));

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_WHITE_FERMENTED_WINE, ModFluids.FLOWING_WHITE_FERMENTED_WINE,
                new SimpleFluidRenderHandler(
                        new Identifier("minecraft:block/water_still"),
                        new Identifier("minecraft:block/water_flow"),
                        0x7BED7F
                ));

        FarmingPlus.LOGGER.info("Registering Mod Fluid Renders for " + FarmingPlus.MOD_ID);
    }
}