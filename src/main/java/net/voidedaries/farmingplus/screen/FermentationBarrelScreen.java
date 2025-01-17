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
import net.minecraft.util.math.BlockPos;
import net.voidedaries.farmingplus.FarmingPlus;
import net.voidedaries.farmingplus.networking.packet.ClearSecondaryFluidC2SPacket;
import net.voidedaries.farmingplus.screen.renderer.FluidStackRenderer;
import net.voidedaries.farmingplus.screen.widgets.IconButtonWidget;
import net.voidedaries.farmingplus.util.FluidStack;
import net.voidedaries.farmingplus.util.MouseUtil;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class FermentationBarrelScreen extends HandledScreen<FermentationBarrelScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(FarmingPlus.MOD_ID, "textures/gui/fermentation_barrel_gui.png");
    private FluidStackRenderer primaryfluidStackRenderer;
    private FluidStackRenderer secondaryFluidStackRenderer;

    public FermentationBarrelScreen(FermentationBarrelScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleY = 6;
        playerInventoryTitleY = 72;
        assignFluidStackRenderers();

        this.addDrawableChild(new IconButtonWidget(
                this.x + 160, this.y + 72, 9, 9,
                new Identifier("farmingplus", "textures/gui/fermentation_barrel_gui.png"),
                176, 28,
                185,28,
                Text.literal("Clear Fluid"),
                button -> clearSecondaryFluidStorage()
        ));
    }

    private void clearSecondaryFluidStorage() {
        BlockPos blockPos = this.getScreenHandler().blockEntity.getPos();

        ClearSecondaryFluidC2SPacket.send(blockPos);
    }



    private void assignFluidStackRenderers() {
        primaryfluidStackRenderer = new FluidStackRenderer(FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 10,
                true, 17, 52);
        secondaryFluidStackRenderer = new FluidStackRenderer(FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 10,
                            true, 17, 52);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        renderFluidTooltip(context, mouseX, mouseY, x, y, handler.primaryfluidStack, 61, 18, primaryfluidStackRenderer);
        renderFluidTooltip(context, mouseX, mouseY, x, y, handler.secondaryFluidStack, 97, 18, secondaryFluidStackRenderer);
    }

    @SuppressWarnings("SameParameterValue")
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
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        renderProgressBar(context, x, y);
        primaryfluidStackRenderer.drawFluid(context, handler.primaryfluidStack, x + 62, y + 18, 16, 52,
                FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 10);
        secondaryFluidStackRenderer.drawFluid(context, handler.secondaryFluidStack,x + 98, y + 18, 16, 52,
                FluidStack.convertDropletsToMb(FluidConstants.BUCKET) * 10);

    }

    private void renderProgressBar(DrawContext context, int x, int y) {
        if(handler.isCrafting()) {
            int progressHeight = handler.getScaledProgress();
            int textureY = 26 - progressHeight;
            context.drawTexture(TEXTURE, x + 83, y + 29 + textureY, 176, textureY, 10, progressHeight);
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
}