package net.voidedaries.farmingplus.block.entity.renderer;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.voidedaries.farmingplus.block.custom.CrushingTub;
import net.voidedaries.farmingplus.block.entity.CrushingTubBlockEntity;
import net.voidedaries.farmingplus.fluid.ModFluids;

public class CrushingTubBlockEntityRenderer implements BlockEntityRenderer<CrushingTubBlockEntity> {
    @SuppressWarnings("unused")
    public CrushingTubBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(CrushingTubBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getWorld() == null || entity.getCachedState() == null) return;

        ItemStack stack = entity.getRenderStack();
        if (!stack.isEmpty()) {
            matrices.push();

            matrices.translate(0.5f, 0.1f, 0.5f);
            matrices.scale(0.5f, 0.5f, 0.5f);

            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(270));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));

            MinecraftClient.getInstance().getItemRenderer().renderItem(
                    stack,
                    ModelTransformationMode.FIXED,
                    getLightLevel(entity.getWorld(), entity.getPos()),
                    OverlayTexture.DEFAULT_UV,
                    matrices,
                    vertexConsumers,
                    entity.getWorld(),
                    1
            );

            matrices.pop();
        }

        Fluid fluid = getFluidType(entity.getCachedState().get(CrushingTub.FLUID_TYPE));
        if (fluid != null) {
            renderFluid(fluid, entity.getPos(),entity, matrices, vertexConsumers, entity.getWorld());
        }
    }

    private void renderFluid(Fluid fluid, BlockPos pos,CrushingTubBlockEntity entity, MatrixStack matrices,VertexConsumerProvider vertexConsumers, World world) {
        if (fluid == null) return;

        BlockState state = world.getBlockState(pos);
        if (!state.isOf(entity.getCachedState().getBlock())) return;

        int fluidLevel = state.get(CrushingTub.FLUID_LEVEL);

        float maxFluidHeight = 7 / 16f;
        float fluidHeight = Math.min((fluidLevel / 16.0f) * 1.1f, maxFluidHeight);

        var fluidRenderHandler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);
        if (fluidRenderHandler == null) return;

        Sprite sprite = fluidRenderHandler.getFluidSprites(world, pos, fluid.getDefaultState())[0];
        int color = fluidRenderHandler.getFluidColor(world, pos, fluid.getDefaultState());

        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        int a = 192;

        matrices.push();
        matrices.translate(0, fluidHeight, 0);
        matrices.scale(1.0f, 1.0f, 1.0f);

        var buffer = vertexConsumers.getBuffer(RenderLayer.getTranslucent());

        buffer.vertex(matrices.peek().getPositionMatrix(), 0, 0, 1).color(r, g, b, a)
                .texture(sprite.getMinU(), sprite.getMinV()).light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
                .normal(matrices.peek().getNormalMatrix(), 0, 1, 0).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 1, 0, 1).color(r, g, b, a)
                .texture(sprite.getMaxU(), sprite.getMinV()).light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
                .normal(matrices.peek().getNormalMatrix(), 0, 1, 0).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 1, 0, 0).color(r, g, b, a)
                .texture(sprite.getMaxU(), sprite.getMaxV()).light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
                .normal(matrices.peek().getNormalMatrix(), 0, 1, 0).next();
        buffer.vertex(matrices.peek().getPositionMatrix(), 0, 0, 0).color(r, g, b, a)
                .texture(sprite.getMinU(), sprite.getMaxV()).light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
                .normal(matrices.peek().getNormalMatrix(), 0, 1, 0).next();

        matrices.pop();
    }

    public static Fluid getFluidType(CrushingTub.GrapeFluidType fluidType) {
         return switch (fluidType) {
            case RED -> ModFluids.STILL_RED_GRAPE_FLUID;
            case BLUE -> ModFluids.STILL_BLUE_GRAPE_FLUID;
            case WHITE -> ModFluids.STILL_WHITE_GRAPE_FLUID;
            case NONE -> null;
        };
    }
    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
