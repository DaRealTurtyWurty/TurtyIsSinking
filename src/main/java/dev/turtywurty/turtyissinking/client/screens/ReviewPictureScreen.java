package dev.turtywurty.turtyissinking.client.screens;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.turtywurty.turtyissinking.TurtyIsSinking;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReviewPictureScreen extends Screen {
    private static final Component TITLE = Component.translatable("screen." + TurtyIsSinking.MOD_ID + ".view_picture");
    private static final Component CANCEL_BUTTON = Component.translatable("button." + TurtyIsSinking.MOD_ID + ".view_picture.cancel");
    private static final Component SAVE_BUTTON = Component.translatable("button." + TurtyIsSinking.MOD_ID + ".view_picture.save");
    private static final Component SAVE_FAILED = Component.translatable("message." + TurtyIsSinking.MOD_ID + ".view_picture.save_failed");
    private static final Component NAME_HINT = Component.translatable("hint." + TurtyIsSinking.MOD_ID + ".view_picture.name");
    private static final Component NAME_NARRATION = Component.translatable("narration." + TurtyIsSinking.MOD_ID + ".view_picture.name");

    private final NativeImage picture;
    private final DynamicTexture texture;
    private final int[][] pixelsCopy;
    private final ResourceLocation location;

    private EditBox nameBox;

    public ReviewPictureScreen(NativeImage picture) {
        super(TITLE);

        this.picture = picture;
        this.texture = new DynamicTexture(picture);
        this.pixelsCopy = new int[picture.getWidth()][picture.getHeight()];
        for (int x = 0; x < picture.getWidth(); x++) {
            for (int y = 0; y < picture.getHeight(); y++) {
                this.pixelsCopy[x][y] = picture.getPixelRGBA(x, y);
            }
        }

        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        this.location = textureManager.register(TurtyIsSinking.MOD_ID + ".camera_image", this.texture);
    }

    @Override
    public void onClose() {
        super.onClose();

        this.texture.close();
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(
                        CANCEL_BUTTON,
                        this::onCancel)
                .bounds(0, this.height - 21, 100, 20)
                .tooltip(Tooltip.create(CANCEL_BUTTON))
                .build());

        this.nameBox = addRenderableWidget(new EditBox(
                this.font,
                this.width / 2 - 100,
                this.height - 21,
                200,
                20,
                NAME_NARRATION
        ));

        this.nameBox.setMaxLength(32);
        this.nameBox.setHint(NAME_HINT);
        this.nameBox.setEditable(true);
        this.nameBox.setValue(String.valueOf(System.currentTimeMillis()));
        setInitialFocus(this.nameBox);

        addRenderableWidget(Button.builder(
                        SAVE_BUTTON,
                        this::onSave)
                .bounds(this.width - 101, this.height - 21, 100, 20)
                .tooltip(Tooltip.create(SAVE_BUTTON))
                .build());
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        drawImage(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
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

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void onCancel(Button ignored) {
        onClose();
    }

    private void onSave(Button ignored) {
        if(this.minecraft == null)
            return;

        Path file = Path.of(this.minecraft.gameDirectory.getAbsolutePath(), "screenshots", TurtyIsSinking.MOD_ID, this.nameBox.getValue() + ".png");
        Util.ioPool().execute(() -> {
            try {
                Files.createDirectories(file.getParent());
                try {
                    this.picture.writeToFile(file);
                } catch (IllegalStateException exception) {
                    var image = new NativeImage(this.picture.getWidth(), this.picture.getHeight(), false);
                    for (int x = 0; x < this.picture.getWidth(); x++) {
                        for (int y = 0; y < this.picture.getHeight(); y++) {
                            image.setPixelRGBA(x, y, this.pixelsCopy[x][y]);
                        }
                    }

                    image.writeToFile(file);
                    image.close();
                }
            } catch (IOException exception) {
                TurtyIsSinking.LOGGER.error("Failed to save picture!", exception);
                if (this.minecraft.player == null)
                    return;

                this.minecraft.player.displayClientMessage(SAVE_FAILED, true);
            } finally {
                this.minecraft.execute(this::onClose);
            }
        });
    }
}
