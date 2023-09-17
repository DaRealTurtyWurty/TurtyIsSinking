package dev.turtywurty.turtyissinking.client.screens;

import com.mojang.blaze3d.platform.NativeImage;
import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.client.screens.widgets.ScrollableGallery;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PictureGalleryScreen extends Screen {
    private static final Component TITLE = Component.translatable("screen." + TurtyIsSinking.MODID + ".view_gallery");
    private static final Component IMAGE_FAILED_TO_LOAD = Component.translatable("screen." + TurtyIsSinking.MODID + ".image_failed_to_load");

    private ScrollableGallery gallery;

    public PictureGalleryScreen() {
        super(TITLE);
    }

    @Override
    protected void init() {
        super.init();

        int width = this.width - 40;
        int height = this.height - 40;
        this.gallery = addRenderableWidget(new ScrollableGallery(
                width,
                height,
                20,
                20,
                4/3f,
                100,
                75,
                10
        ));

        CompletableFuture<List<ScrollableGallery.GalleryEntry>> futureEntries = new CompletableFuture<>();
        Util.ioPool().execute(() -> {
            if (this.minecraft == null) {
                futureEntries.completeExceptionally(new IllegalStateException("Minecraft is null!"));
                return;
            }

            try {
                Path galleryPath = Path.of(this.minecraft.gameDirectory.getAbsolutePath(), "screenshots", TurtyIsSinking.MODID);
                Files.createDirectories(galleryPath);

                List<ScrollableGallery.GalleryEntry> entries = new ArrayList<>();
                for (Path path : Files.list(galleryPath).toList()) {
                    try {
                        InputStream imageStream = Files.newInputStream(path);
                        NativeImage image = NativeImage.read(imageStream);

                        entries.add(new ScrollableGallery.GalleryEntry(path, image));
                    } catch (IOException exception) {
                        this.minecraft.execute(() -> this.minecraft.player.displayClientMessage(IMAGE_FAILED_TO_LOAD, false));
                    }
                };

                futureEntries.complete(entries);
            } catch (IOException exception) {
                futureEntries.completeExceptionally(exception);
            }
        });

        futureEntries.thenAccept(this.gallery::setEntries);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
