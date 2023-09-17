package dev.turtywurty.turtyissinking.client;

import com.mojang.blaze3d.platform.NativeImage;
import dev.turtywurty.turtyissinking.client.screens.ReviewPictureScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;

import java.util.concurrent.atomic.AtomicBoolean;

public class ClientAccess {
    private static final AtomicBoolean IS_TAKING_PICTURE = new AtomicBoolean(false);

    public static void executeTakePicture() {
        Minecraft minecraft = Minecraft.getInstance();
        NativeImage image = Screenshot.takeScreenshot(minecraft.getMainRenderTarget());
        minecraft.setScreen(new ReviewPictureScreen(image));
        IS_TAKING_PICTURE.set(false);
    }

    public static boolean isTakingPicture() {
        return IS_TAKING_PICTURE.get();
    }

    public static void takePicture() {
        IS_TAKING_PICTURE.set(true);
    }
}
