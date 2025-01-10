package net.voidedaries.farmingplus;

import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.block.ModBlocks;
import net.voidedaries.farmingplus.block.entity.ModBlockEntities;
import net.voidedaries.farmingplus.block.entity.renderer.CrushingTubBlockEntityRenderer;
import net.voidedaries.farmingplus.client.SaturationHudOverlay;
import net.voidedaries.farmingplus.effect.ModEffects;
import net.voidedaries.farmingplus.fluid.ModFluidRenderHandlers;
import net.voidedaries.farmingplus.fluid.ModFluids;
import net.voidedaries.farmingplus.networking.ModMessages;
import net.voidedaries.farmingplus.screen.BottlerScreen;
import net.voidedaries.farmingplus.screen.FermentationBarrelScreen;
import net.voidedaries.farmingplus.screen.ModScreenHandlers;
import net.voidedaries.farmingplus.screen.SeedBagScreen;

public class FarmingPlusClient implements ClientModInitializer {
    public static final Identifier SPLIT_ID = new Identifier(FarmingPlus.MOD_ID,"shaders/post/double_vision.json");
    private final ManagedShaderEffect SplitShader = ShaderEffectManager.getInstance().manage(SPLIT_ID);

    @Override
    public void onInitializeClient() {

        //Client Packets
        ModMessages.registerS2CPackets();

        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) {
                return;
            }

            if (client.player.hasStatusEffect(ModEffects.INTOXICATION)) {
                SplitShader.render(tickDelta);
            }
        });

        //Hud Overlays
        HudRenderCallback.EVENT.register(new SaturationHudOverlay());

        //Screen Handlers
        HandledScreens.register(ModScreenHandlers.FERMENTATION_BARREL_SCREEN_HANDLER, FermentationBarrelScreen::new);

        HandledScreens.register(ModScreenHandlers.BOTTLER_SCREEN_HANDLER, BottlerScreen::new);

        HandledScreens.register(ModScreenHandlers.SEED_BAG_SCREEN_HANDLER, SeedBagScreen::new);

        BlockEntityRendererFactories.register(ModBlockEntities.CRUSHING_TUB_BLOCK_ENTITY, CrushingTubBlockEntityRenderer::new);

        //Biome Colour Hue
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world != null && pos != null) {
                return BiomeColors.getGrassColor(world, pos);
            }
            return 0xFFFFFF;
        }, ModBlocks.RED_GRAPE_VINE, ModBlocks.BLUE_GRAPE_VINE, ModBlocks.WHITE_GRAPE_VINE);

        ModFluidRenderHandlers.registerFluidRenders();

        //region Solids
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getSolid(),
                ModFluids.STILL_RED_GRAPE_FLUID, ModFluids.FLOWING_RED_GRAPE_FLUID);

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getSolid(),
                ModFluids.STILL_BLUE_GRAPE_FLUID, ModFluids.FLOWING_BLUE_GRAPE_FLUID);

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getSolid(),
                ModFluids.STILL_WHITE_GRAPE_FLUID, ModFluids.FLOWING_WHITE_GRAPE_FLUID);

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getSolid(),
                ModFluids.STILL_RED_FERMENTED_WINE, ModFluids.FLOWING_RED_FERMENTED_WINE);

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getSolid(),
                ModFluids.STILL_BLUE_FERMENTED_WINE, ModFluids.FLOWING_BLUE_FERMENTED_WINE);

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getSolid(),
                ModFluids.STILL_WHITE_FERMENTED_WINE, ModFluids.FLOWING_WHITE_FERMENTED_WINE);
        //endregion

        //region Translucency
        BlockRenderLayerMap.INSTANCE.putFluids(
                RenderLayer.getTranslucent(),
                ModFluids.STILL_RED_GRAPE_FLUID,
                ModFluids.FLOWING_RED_GRAPE_FLUID,
                ModFluids.STILL_BLUE_GRAPE_FLUID,
                ModFluids.FLOWING_BLUE_GRAPE_FLUID,
                ModFluids.STILL_WHITE_GRAPE_FLUID,
                ModFluids.FLOWING_WHITE_GRAPE_FLUID
        );

        BlockRenderLayerMap.INSTANCE.putFluid(ModFluids.STILL_RED_GRAPE_FLUID, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putFluid(ModFluids.FLOWING_RED_GRAPE_FLUID, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putFluid(ModFluids.STILL_BLUE_GRAPE_FLUID, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putFluid(ModFluids.FLOWING_BLUE_GRAPE_FLUID, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putFluid(ModFluids.STILL_WHITE_GRAPE_FLUID, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putFluid(ModFluids.FLOWING_WHITE_GRAPE_FLUID, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putFluid(ModFluids.STILL_RED_FERMENTED_WINE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putFluid(ModFluids.FLOWING_RED_FERMENTED_WINE, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putFluid(ModFluids.STILL_BLUE_FERMENTED_WINE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putFluid(ModFluids.FLOWING_BLUE_FERMENTED_WINE, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putFluid(ModFluids.STILL_WHITE_FERMENTED_WINE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putFluid(ModFluids.FLOWING_WHITE_FERMENTED_WINE, RenderLayer.getTranslucent());
        //endregion

        //region Cutouts
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BOTTLER, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHERRY_FRUIT_LEAVES, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RED_GRAPE_VINE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUE_GRAPE_VINE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WHITE_GRAPE_VINE, RenderLayer.getCutout());
        //endregion
    }
}
