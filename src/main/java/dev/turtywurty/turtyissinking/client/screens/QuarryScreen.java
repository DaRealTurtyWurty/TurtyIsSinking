package dev.turtywurty.turtyissinking.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class QuarryScreen extends Screen {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(TurtyIsSinking.MODID,
        "textures/gui/quarry.png");

    private int leftPos, topPos, imageWidth, imageHeight, textureWidth, textureHeight;

    private Button widthDecrease, widthIncrease, heightDecrease, heightIncrease;
    private Button xOffsetDecrease, xOffsetIncrease, yOffsetDecrease, yOffsetIncrease, zOffsetDecrease, zOffsetIncrease;
    private EditBox widthInput, heightInput, xOffsetInput, yOffsetInput, zOffsetInput;

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
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        blit(pPoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.textureWidth,
            this.textureHeight);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        this.font.draw(pPoseStack, this.title, this.leftPos + 8, this.topPos + 5, 0x404040);
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        this.widthDecrease = addRenderableWidget(new Button(this.leftPos + 10, this.topPos + 20, 12, 16,
            Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.width_decrease"), btn -> {
            }));

        this.widthIncrease = addRenderableWidget(new Button(this.leftPos + 57, this.topPos + 20, 12, 16,
            Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.width_increase"), btn -> {
            }));

        this.widthInput = addRenderableWidget(new EditBox(this.font, this.leftPos + 22, this.topPos + 20, 35, 16,
            Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.width_input")));
        
        this.heightDecrease = addRenderableWidget(new Button(this.leftPos + 10, this.topPos + 40, 12, 16,
            Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.height_decrease"), btn -> {
            }));

        this.heightIncrease = addRenderableWidget(new Button(this.leftPos + 57, this.topPos + 40, 12, 16,
            Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.height_increase"), btn -> {
            }));
        
        this.heightInput = addRenderableWidget(new EditBox(this.font, this.leftPos + 22, this.topPos + 40, 35, 16,
            Component.translatable("screen." + TurtyIsSinking.MODID + ".quarry.height_input")));
    }
}
