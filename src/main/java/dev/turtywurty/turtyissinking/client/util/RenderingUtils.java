package dev.turtywurty.turtyissinking.client.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public final class RenderingUtils {
    private static final ResourceLocation VIGNETTE_LOCATION = new ResourceLocation("textures/misc/vignette.png");
    private static final ResourceLocation CIRCLE_LOCATION = new ResourceLocation(TurtyIsSinking.MODID,
        "textures/misc/shadow.png");

    public static void renderCircle(PoseStack poseStack, int x, int y, int size, int red, int green, int blue,
        float alpha) {
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderColor(red, green, blue, alpha);
        RenderSystem.setShaderTexture(0, CIRCLE_LOCATION);
        GuiComponent.blit(poseStack, x, y, 0, 0, size, size, size, size);
    }

    public static void renderCorners(PoseStack poseStack, int screenWidth, int screenHeight, int xOffset, int yOffset,
        int length, int thickness, int argb) {
        // Top Left
        GuiComponent.fill(poseStack, xOffset, yOffset, xOffset + length, yOffset + thickness, argb);
        GuiComponent.fill(poseStack, xOffset, yOffset, xOffset + thickness, yOffset + length, argb);

        // Top Right
        GuiComponent.fill(poseStack, screenWidth - length - xOffset, yOffset, screenWidth - length - xOffset + length,
            yOffset + thickness, argb);
        GuiComponent.fill(poseStack, screenWidth - xOffset, yOffset, screenWidth - xOffset + thickness,
            yOffset + length, argb);

        // Bottom Left
        GuiComponent.fill(poseStack, xOffset, screenHeight - yOffset, xOffset + length,
            screenHeight - yOffset + thickness, argb);
        GuiComponent.fill(poseStack, xOffset, screenHeight - length - yOffset, xOffset + thickness,
            screenHeight - yOffset + thickness, argb);

        // Bottom Right
        GuiComponent.fill(poseStack, screenWidth - length - xOffset, screenHeight - yOffset,
            screenWidth - length - xOffset + length, screenHeight - yOffset + thickness, argb);
        GuiComponent.fill(poseStack, screenWidth - xOffset, screenHeight - length - yOffset,
            screenWidth - xOffset + thickness, screenHeight - yOffset + thickness, argb);
    }

    public static void renderCrosshair(PoseStack poseStack, int x, int y, int length, int thickness, int argb) {
        GuiComponent.fill(poseStack, x - length, y - thickness / 2, x + length, y + thickness / 2, argb);
        GuiComponent.fill(poseStack, x - thickness / 2, y - length, x + thickness / 2, y + length, argb);
    }
    
    public static void renderVignette(int screenWidth, int screenHeight, float strength) {
        renderVignette(screenWidth, screenHeight, strength, strength, strength, 1.0f);
    }

    public static void renderVignette(int screenWidth, int screenHeight, float redStrength, float greenStrength,
        float blueStrength, float alpha) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
            GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(redStrength, greenStrength, blueStrength, alpha);
        
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, VIGNETTE_LOCATION);
        final Tesselator tesselator = Tesselator.getInstance();
        final BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(0.0D, screenHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex(screenWidth, screenHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(screenWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
        tesselator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
    }
}
