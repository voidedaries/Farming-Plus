package net.voidedaries.farmingplus.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.screen.renderer.FluidStackRenderer;
import net.voidedaries.farmingplus.util.FluidStack;
import net.voidedaries.farmingplus.util.MouseUtil;

import java.util.List;
import java.util.stream.Collectors;

public class BottlerScreen extends HandledScreen<BottlerScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(FarmingPlus.MOD_ID, "textures/gui/bottler_gui.png");
    private FluidStackRenderer fluidStackRenderer;

    public BottlerScreen(BottlerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleY = 6;
        playerInventoryTitleY = 73;
        assignFluidStackRenderer();
    }

    private void assignFluidStackRenderer() {
        fluidStackRenderer = new FluidStackRenderer(FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 10,
                true, 15, 51);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        renderFluidTooltip(context, mouseX, mouseY, x, y, handler.fluidStack, 152, 18, fluidStackRenderer);
    }

    private void renderFluidTooltip(DrawContext context, int mouseX, int mouseY, int x, int y,
                                    FluidStack fluidStack, int offsetX, int offsetY, FluidStackRenderer renderer) {
        if(isMouseAboveArea(mouseX, mouseY, x, y, offsetX, offsetY, renderer)) {
            List<Text> tooltip = renderer.getTooltip(fluidStack, TooltipContext.Default.BASIC);

            List<OrderedText> orderedTooltip = tooltip.stream()
                    .map(Text::asOrderedText)
                    .collect(Collectors.toList());

            MinecraftClient client = MinecraftClient.getInstance();
            context.drawOrderedTooltip(client.textRenderer, orderedTooltip, mouseX - x, mouseY - y);
        }
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f,1f,1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        renderProgressArrow(context, x, y);
        fluidStackRenderer.drawFluid(context, handler.fluidStack, x + 152, y + 18, 16, 52,
                FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 10);
    }

    private void renderProgressArrow(DrawContext context, int x, int y) {
        if (handler.isCrafting()) {
            int progressWidth = handler.getScaledProgress();
            context.drawTexture(TEXTURE, x + 119, y + 56, 176, 0, progressWidth, 9);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMousex, int pMousey, int x, int y, int offsetX, int offsetY, FluidStackRenderer renderer) {
        return MouseUtil.isMouseOver(pMousex, pMousey, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }

    private boolean isMouseAboveArea(int pMousex, int pMousey, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMousex, pMousey, x + offsetX, y + offsetY, width, height);
    }
}
