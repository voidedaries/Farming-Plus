package net.voidedaries.farmingplus.screen.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class IconButtonWidget extends ButtonWidget {
    private final Identifier texture;
    private final int u, v;
    private final int hoverU, hoverV;
    private final Text tooltip;

    public IconButtonWidget(int x, int y, int width, int height, Identifier texture, int u, int v, int hoverU, int hoverV, Text tooltip, PressAction onPress) {
        super(x, y, width, height, tooltip, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.hoverU = hoverU;
        this.hoverV = hoverV;
        this.tooltip = tooltip;
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, texture);

        int renderU = this.isHovered() ? hoverU : u;
        int renderV = this.isHovered() ? hoverV : v;

        context.drawTexture(texture, this.getX(), this.getY(), renderU, renderV, this.width, this.height);

        if (this.isHovered() && !tooltip.getString().isEmpty()) {
            MinecraftClient client = MinecraftClient.getInstance();
            context.drawOrderedTooltip(
                    client.textRenderer,
                    client.textRenderer.wrapLines(tooltip, 200), // Automatically wraps tooltip
                    mouseX, mouseY
            );
        }
    }
}
