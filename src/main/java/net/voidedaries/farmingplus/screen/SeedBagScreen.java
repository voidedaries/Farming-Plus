package net.voidedaries.farmingplus.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.FarmingPlus;

public class SeedBagScreen extends HandledScreen<SeedBagScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(FarmingPlus.MOD_ID, "textures/gui/seed_bag_gui.png");

    public SeedBagScreen(SeedBagScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        if (this.focusedSlot != null && this.focusedSlot.hasStack()) {
            context.drawItemTooltip(this.textRenderer, this.focusedSlot.getStack(), mouseX, mouseY);
        }
    }

    @Override
    protected void init() {
        super.init();
        titleY = 6;
        playerInventoryTitleY = 55;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f,1f,1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }
}
