package net.voidedaries.farmingplus.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import net.voidedaries.farmingplus.FarmingPlus;

public class SaturationHudOverlay implements HudRenderCallback {
    private static final Identifier FULL_SATURATION = new Identifier(FarmingPlus.MOD_ID,
            "textures/gui/saturation_full.png");
    private static final Identifier HALF_SATURATION = new Identifier(FarmingPlus.MOD_ID,
            "textures/gui/saturation_half.png");


    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.player != null) {
            PlayerEntity player = client.player;
            int saturationLevel = (int) player.getHungerManager().getSaturationLevel();

            @SuppressWarnings("DataFlowIssue") GameMode gameMode = client.interactionManager.getCurrentGameMode();
            if (client.interactionManager.getCurrentGameMode() != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE) {
                return;
            }

            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            int x = width / 2 + 82;
            int y = height - 39;

            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            for (int i = 0; i < 10; i++) {
                int iconX = x - i * 8;
                if (saturationLevel >= (i + 1) * 2) {
                    RenderSystem.setShaderTexture(0, FULL_SATURATION);
                    drawContext.drawTexture(FULL_SATURATION, iconX, y, 0, 0, 9, 9, 9, 9);
                } else if (saturationLevel >= (i + 0.5) * 2) {
                    RenderSystem.setShaderTexture(0, HALF_SATURATION);
                    drawContext.drawTexture(HALF_SATURATION, iconX, y, 0, 0, 9, 9, 9, 9);
                }
            }
        }
    }
}
