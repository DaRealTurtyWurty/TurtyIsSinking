package dev.turtywurty.turtyissinking.client.screens;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.turtywurty.turtyissinking.TurtyIsSinking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class EditImageScreen extends Screen {
    private static final Component TITLE = Component.translatable("screen." + TurtyIsSinking.MOD_ID + ".edit_image");
    private static final Component BACK_BUTTON = Component.translatable("button." + TurtyIsSinking.MOD_ID + "edit_image.back");

    private final NativeImage picture;
    private final Path picturePath;

    private final DynamicTexture texture;
    private final int[][] pixelsCopy;
    private final ResourceLocation location;

    public EditImageScreen(NativeImage picture, Path picturePath) {
        super(TITLE);

        this.picture = picture;
        this.picturePath = picturePath;

        this.texture = new DynamicTexture(picture);
        this.pixelsCopy = new int[picture.getWidth()][picture.getHeight()];
        for (int x = 0; x < picture.getWidth(); x++) {
            for (int y = 0; y < picture.getHeight(); y++) {
                this.pixelsCopy[x][y] = picture.getPixelRGBA(x, y);
            }
        }

        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        this.location = textureManager.register(TurtyIsSinking.MOD_ID + ".edit_camera_image", this.texture);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(Button.builder(
                BACK_BUTTON,
                ignored -> {
                    if(this.minecraft != null)
                        this.minecraft.popGuiLayer();
                })
                .bounds(0, this.height - 21, 100, 20)
                .build());
    }

    @Override
    public void onClose() {
        super.onClose();

        this.texture.upload();
        this.texture.close();
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        drawImage(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        String name = this.picturePath.getFileName().toString();
        drawCenteredString(pPoseStack, this.font, name, this.width / 2, this.height - 40, 0xFFFFFF);
    }

    private void drawImage(PoseStack poseStack) {
        RenderSystem.setShaderTexture(0, this.location);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        // calculate the width and height whilst maintaining the aspect ratio
        float aspectRatio = (float) this.picture.getWidth() / (float) this.picture.getHeight();
        int width = this.width - 60;
        int height = (int) (width / aspectRatio);

        // if the height is too big, then calculate the width whilst maintaining the aspect ratio
        if (height > this.height - 60) {
            height = this.height - 60;
            width = (int) (height * aspectRatio);
        }

        // center the image
        int x = (this.width - width) / 2;
        int y = (this.height - height) / 2;

        blit(poseStack,
                x,
                y,
                width,
                height,
                0,
                0,
                this.picture.getWidth(),
                this.picture.getHeight(),
                this.picture.getWidth(),
                this.picture.getHeight());
    }
}
