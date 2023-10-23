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
    private static final ResourceLocation BACKGROUND = new ResourceLocation(TurtyIsSinking.MODID,
        "textures/gui/quarry.png");

    private static final Component WIDTH_INPUT_MESSAGE =
            Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.width_input");
    private static final Component WIDTH_DECREASE_MESSAGE =
            Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.width_decrease");
    private static final Component WIDTH_INCREASE_MESSAGE =
            Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.width_increase");

    private static final Component HEIGHT_INPUT_MESSAGE =
            Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.height_input");
    private static final Component HEIGHT_DECREASE_MESSAGE =
            Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.height_decrease");
    private static final Component HEIGHT_INCREASE_MESSAGE =
            Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.height_increase");
    
    private int leftPos;
    private int topPos;
    private final int imageWidth, imageHeight, textureWidth, textureHeight;
    
    private ListenerButton widthDecrease, widthIncrease, heightDecrease, heightIncrease;
    private ListenerButton xOffsetDecrease, xOffsetIncrease, yOffsetDecrease, yOffsetIncrease, zOffsetDecrease, zOffsetIncrease;
    private BarrelNumberEditBox widthInput, heightInput, xOffsetInput, yOffsetInput, zOffsetInput;
    
    public QuarryScreen() {
        super(Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry"));
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

//        this.widthInput = addRenderableWidget(new EditBox(this.font, this.leftPos + 22, this.topPos + 20, 35, 16,
//                Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.width_input")));
//
//        Button widthDecreaseButton = new Button.Builder(Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.width_decrease"), (btn) -> {}).pos(this.leftPos + 10, this.topPos + 19).size(12, 18).build();
//        this.widthDecrease = addRenderableWidget(widthDecreaseButton);
//
//        Button widthIncreaseButton = new Button.Builder(Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.width_increase"), (btn) -> {}).pos(this.leftPos + 57, this.topPos + 19).size(12, 18).build();
//        this.widthIncrease = addRenderableWidget(widthIncreaseButton);
//
//        this.heightInput = addRenderableWidget(new EditBox(this.font, this.leftPos + 22, this.topPos + 40, 35, 16,
//                Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.height_input")));
//
//        Button heightDecreaseButton = new Button.Builder(Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.height_decrease"), (btn) -> {}).pos(this.leftPos + 10, this.topPos + 39).size(12, 18).build();
//        this.heightDecrease = addRenderableWidget(heightDecreaseButton);
//
//        Button heightIncreaseButton = new Button.Builder(Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.height_increase"), (btn) -> {}).pos(this.leftPos + 57, this.topPos + 39).size(12, 18).build();
//        this.heightIncrease = addRenderableWidget(heightIncreaseButton);

        this.widthDecrease = addRenderableWidget(new ListenerButton(
                new Button.Builder(WIDTH_DECREASE_MESSAGE, (btn) -> {})
                        .bounds(this.leftPos + 10, this.topPos + 19, 12, 18)));

        this.widthIncrease = addRenderableWidget(new ListenerButton(
                new Button.Builder(WIDTH_INCREASE_MESSAGE, (btn) -> {})
                        .bounds(this.leftPos + 57, this.topPos + 19, 12, 18)));

        this.widthInput = addRenderableWidget(
                new BarrelNumberEditBox.Builder(this.font, WIDTH_INPUT_MESSAGE, this.widthDecrease, this.widthIncrease)
                        .bounds(this.leftPos + 22, this.topPos + 20, 35, 16)
                        .minValue(1)
                        .maxValue(256)
                        .defaultValue(16)
                        .build());

        this.heightDecrease = addRenderableWidget(new ListenerButton(
                new Button.Builder(HEIGHT_DECREASE_MESSAGE, (btn) -> {})
                        .bounds(this.leftPos + 10, this.topPos + 39, 12, 18)));

        this.heightIncrease = addRenderableWidget(new ListenerButton(
                new Button.Builder(HEIGHT_INCREASE_MESSAGE, (btn) -> {})
                        .bounds(this.leftPos + 57, this.topPos + 39, 12, 18)));

        this.heightInput = addRenderableWidget(
                new BarrelNumberEditBox.Builder(this.font, HEIGHT_INPUT_MESSAGE, this.heightDecrease, this.heightIncrease)
                        .bounds(this.leftPos + 22, this.topPos + 40, 35, 16)
                        .minValue(1)
                        .maxValue(256)
                        .defaultValue(16)
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
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
