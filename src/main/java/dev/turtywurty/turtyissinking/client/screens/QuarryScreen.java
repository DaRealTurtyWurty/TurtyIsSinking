package dev.turtywurty.turtyissinking.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.client.screens.widgets.BarrelNumberEditBox;
import dev.turtywurty.turtyissinking.client.screens.widgets.ListenerButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class QuarryScreen extends Screen {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(TurtyIsSinking.MOD_ID,
        "textures/gui/quarry.png");

    private static final Component WIDTH_INPUT_MESSAGE =
            Component.translatable("screen." + TurtyIsSinking.MOD_ID + ".quarry.width_input");
    private static final Component WIDTH_DECREASE_MESSAGE =
            Component.translatable("screen." + TurtyIsSinking.MOD_ID + ".quarry.width_decrease");
    private static final Component WIDTH_INCREASE_MESSAGE =
            Component.translatable("screen." + TurtyIsSinking.MOD_ID + ".quarry.width_increase");

    private static final Component HEIGHT_INPUT_MESSAGE =
            Component.translatable("screen." + TurtyIsSinking.MOD_ID + ".quarry.height_input");
    private static final Component HEIGHT_DECREASE_MESSAGE =
            Component.translatable("screen." + TurtyIsSinking.MOD_ID + ".quarry.height_decrease");
    private static final Component HEIGHT_INCREASE_MESSAGE =
            Component.translatable("screen." + TurtyIsSinking.MOD_ID + ".quarry.height_increase");

    private static final Component WIDTH_LABEL =
            Component.translatable("screen." + TurtyIsSinking.MOD_ID + ".quarry.width");

    private static final Component HEIGHT_LABEL =
            Component.translatable("screen." + TurtyIsSinking.MOD_ID + ".quarry.height");

    private int leftPos;
    private int topPos;
    private final int imageWidth, imageHeight, textureWidth, textureHeight;
    
    private ListenerButton widthDecrease, widthIncrease, heightDecrease, heightIncrease;
    private ListenerButton xOffsetDecrease, xOffsetIncrease, yOffsetDecrease, yOffsetIncrease, zOffsetDecrease, zOffsetIncrease;
    private BarrelNumberEditBox widthInput, heightInput, xOffsetInput, yOffsetInput, zOffsetInput;
    
    public QuarryScreen() {
        super(Component.translatable("screen." + TurtyIsSinking.MOD_ID + ".quarry"));
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.textureWidth = 256;
        this.textureHeight = 256;
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        this.widthDecrease = addRenderableWidget(new ListenerButton(
                new Button.Builder(WIDTH_DECREASE_MESSAGE, (btn) -> {})
                        .bounds(this.leftPos + 10, this.topPos + 28, 12, 20)));

        this.widthIncrease = addRenderableWidget(new ListenerButton(
                new Button.Builder(WIDTH_INCREASE_MESSAGE, (btn) -> {})
                        .bounds(this.leftPos + 59, this.topPos + 28, 12, 20)));

        this.widthInput = addRenderableWidget(
                new BarrelNumberEditBox.Builder(this.font, WIDTH_INPUT_MESSAGE, this.widthDecrease, this.widthIncrease)
                        .bounds(this.leftPos + 23, this.topPos + 30, 35, 16)
                        .minValue(1)
                        .maxValue(256)
                        .defaultValue(16)
                        .build());

        this.heightDecrease = addRenderableWidget(new ListenerButton(
                new Button.Builder(HEIGHT_DECREASE_MESSAGE, (btn) -> {})
                        .bounds(this.leftPos + 10, this.topPos + 58, 12, 20)));

        this.heightIncrease = addRenderableWidget(new ListenerButton(
                new Button.Builder(HEIGHT_INCREASE_MESSAGE, (btn) -> {})
                        .bounds(this.leftPos + 59, this.topPos + 58, 12, 20)));

        this.heightInput = addRenderableWidget(
                new BarrelNumberEditBox.Builder(this.font, HEIGHT_INPUT_MESSAGE, this.heightDecrease, this.heightIncrease)
                        .bounds(this.leftPos + 23, this.topPos + 60, 35, 16)
                        .minValue(1)
                        .maxValue(256)
                        .defaultValue(16)
                        .build());

        this.xOffsetDecrease = addRenderableWidget(new ListenerButton(
                new Button.Builder(Component.literal("<"), (btn) -> {})
                        .bounds(this.leftPos + 10, this.topPos + 88, 12, 20)));

        this.xOffsetIncrease = addRenderableWidget(new ListenerButton(
                new Button.Builder(Component.literal(">"), (btn) -> {})
                        .bounds(this.leftPos + 59, this.topPos + 88, 12, 20)));

        this.xOffsetInput = addRenderableWidget(
                new BarrelNumberEditBox.Builder(this.font, Component.literal("X"), this.xOffsetDecrease, this.xOffsetIncrease)
                        .bounds(this.leftPos + 23, this.topPos + 90, 35, 16)
                        .minValue(-256)
                        .maxValue(256)
                        .defaultValue(0)
                        .build());

        this.yOffsetDecrease = addRenderableWidget(new ListenerButton(
                new Button.Builder(Component.literal("<"), (btn) -> {})
                        .bounds(this.leftPos + 10, this.topPos + 118, 12, 20)));

        this.yOffsetIncrease = addRenderableWidget(new ListenerButton(
                new Button.Builder(Component.literal(">"), (btn) -> {})
                        .bounds(this.leftPos + 59, this.topPos + 118, 12, 20)));

        this.yOffsetInput = addRenderableWidget(
                new BarrelNumberEditBox.Builder(this.font, Component.literal("Y"), this.yOffsetDecrease, this.yOffsetIncrease)
                        .bounds(this.leftPos + 23, this.topPos + 120, 35, 16)
                        .minValue(-256)
                        .maxValue(256)
                        .defaultValue(0)
                        .build());

        this.zOffsetDecrease = addRenderableWidget(new ListenerButton(
                new Button.Builder(Component.literal("<"), (btn) -> {})
                        .bounds(this.leftPos + 10, this.topPos + 148, 12, 20)));

        this.zOffsetIncrease = addRenderableWidget(new ListenerButton(
                new Button.Builder(Component.literal(">"), (btn) -> {})
                        .bounds(this.leftPos + 59, this.topPos + 148, 12, 20)));

        this.zOffsetInput = addRenderableWidget(
                new BarrelNumberEditBox.Builder(this.font, Component.literal("Z"), this.zOffsetDecrease, this.zOffsetIncrease)
                        .bounds(this.leftPos + 23, this.topPos + 150, 35, 16)
                        .minValue(-256)
                        .maxValue(256)
                        .defaultValue(0)
                        .build());
    }
    
    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        blit(pPoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.textureWidth,
            this.textureHeight);
        
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        
        this.font.draw(pPoseStack, this.title, this.leftPos + 8, this.topPos + 5, 0x404040);

        this.font.draw(pPoseStack, WIDTH_LABEL, this.leftPos + 8, this.topPos + 20, 0x404040);
        this.font.draw(pPoseStack, HEIGHT_LABEL, this.leftPos + 8, this.topPos + 50, 0x404040);

        this.font.draw(pPoseStack, Component.literal("Offset"), this.leftPos + 8, this.topPos + 80, 0x404040);
        this.font.draw(pPoseStack, Component.literal("X"), this.leftPos + 8, this.topPos + 110, 0x404040);
        this.font.draw(pPoseStack, Component.literal("Y"), this.leftPos + 8, this.topPos + 140, 0x404040);
        this.font.draw(pPoseStack, Component.literal("Z"), this.leftPos + 8, this.topPos + 170, 0x404040);

    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
