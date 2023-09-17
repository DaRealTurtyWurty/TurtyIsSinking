package dev.turtywurty.turtyissinking.client.screens.widgets;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.client.screens.EditImageScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.*;

public class ScrollableGallery extends ScrollPanel {
    private final List<GalleryEntry> entries = new ArrayList<>();
    private final float itemAspectRatio;
    private final int itemWidth, itemHeight, itemPadding;

    public ScrollableGallery(int width, int height, int top, int left, float itemAspectRatio, int itemWidth, int itemHeight, int itemPadding) {
        super(Minecraft.getInstance(), width, height, top, left);
        this.itemAspectRatio = itemAspectRatio;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        this.itemPadding = itemPadding;
    }

    public void setEntries(Collection<GalleryEntry> entries) {
        this.entries.clear();
        this.entries.addAll(entries);
        this.entries.sort(Comparator.comparing(entry -> entry.path().getFileName().toString()));
    }

    @Override
    protected int getContentHeight() {
        int numColumns = this.width / (this.itemWidth + this.itemPadding);
        int numRows = (int) Math.ceil(this.entries.size() / (float) numColumns);
        return numRows * (this.itemHeight + this.itemPadding);
    }

    @Override
    protected void drawPanel(PoseStack poseStack, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
        int numColumns = this.width / (this.itemWidth + this.itemPadding);
        int itemHeight = (int) (this.itemHeight / this.itemAspectRatio);

        final int totalRowWidth = (numColumns * itemWidth) + ((numColumns - 1) * itemPadding);
        final int startX = this.left + (this.width - totalRowWidth) / 2;

        int x = startX;
        int y = this.top + relativeY + this.itemPadding;

        for (GalleryEntry entry : this.entries) {
            drawEntry(entry, poseStack, x, y, this.itemWidth, itemHeight, mouseX, mouseY);

            x += this.itemWidth + this.itemPadding;
            if (x + this.itemWidth + this.itemPadding > this.width) {
                x = startX;
                y += this.itemPadding + itemHeight;
            }
        }
    }

    private static boolean isMouseOver(int x, int y, int width, int height, double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private boolean isOverEntry(int index, double mouseX, double mouseY) {
        int numColumns = this.width / (this.itemWidth + this.itemPadding);
        int itemHeight = (int) (this.itemHeight / this.itemAspectRatio);

        final int totalRowWidth = (numColumns * itemWidth) + ((numColumns - 1) * itemPadding);
        final int startX = this.left + (this.width - totalRowWidth) / 2;

        int x = startX;
        int y = this.top + this.itemPadding;

        for (int i = 0; i < this.entries.size(); i++) {
            if (i == index) {
                return isMouseOver(x, y, this.itemWidth, itemHeight, mouseX, mouseY);
            }

            x += this.itemWidth + this.itemPadding;
            if (x + this.itemWidth + this.itemPadding > this.width) {
                x = startX;
                y += this.itemPadding + itemHeight;
            }
        }

        return false;
    }

    protected void drawEntry(GalleryEntry entry, PoseStack poseStack, int entryLeft, int entryTop, int entryWidth, int entryHeight, int mouseX, int mouseY) {
        fill(poseStack,
                entryLeft,
                entryTop,
                entryLeft + entryWidth,
                entryTop + entryHeight,
                isOverEntry(this.entries.indexOf(entry), mouseX, mouseY) ? 0x80FFFFFF : 0x80CCCCCC);

        ResourceLocation textureLocation = entry.textureLocation();
        NativeImage image = entry.image();

        RenderSystem.setShaderTexture(0, textureLocation);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        blit(poseStack,
                entryLeft + 1,
                entryTop + 1,
                entryWidth - 2,
                entryHeight - 2,
                0,
                0,
                image.getWidth(),
                image.getHeight(),
                image.getWidth(),
                image.getHeight());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (int index = 0; index < this.entries.size(); index++) {
                if (isOverEntry(index, mouseX, mouseY)) {
                    GalleryEntry entry = this.entries.get(index);
                    Minecraft.getInstance().pushGuiLayer(new EditImageScreen(entry.image(), entry.path()));
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
        pNarrationElementOutput.add(NarratedElementType.TITLE, "Gallery");
    }

    public static class GalleryEntry {
        private final Path path;
        private final NativeImage image;

        private final DynamicTexture texture;
        private final ResourceLocation textureLocation;

        public GalleryEntry(Path path, NativeImage image) {
            this.path = path;
            this.image = image;

            String name = path.getFileName().toString();

            TextureManager manager = Minecraft.getInstance().getTextureManager();
            this.textureLocation = manager.register(
                    TurtyIsSinking.MODID + ".camera_image_" + name,
                    this.texture = new DynamicTexture(this.image));
        }

        public Path path() {
            return this.path;
        }

        public NativeImage image() {
            return this.image;
        }

        public DynamicTexture texture() {
            return this.texture;
        }

        public ResourceLocation textureLocation() {
            return this.textureLocation;
        }
    }
}
