package dev.turtywurty.turtyissinking.mixins.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.turtywurty.turtyissinking.client.util.RenderingUtils;
import dev.turtywurty.turtyissinking.items.NitroCanisterItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Shadow
    private void fillRect(BufferBuilder pRenderer, int pX, int pY, int pWidth, int pHeight, int pRed, int pGreen, int pBlue, int pAlpha) {}

    @Inject(
            method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isBarVisible()Z"
            )
    )
    private void turtyIsSinking$renderNitroBar(Font pFont, ItemStack pStack, int pXPosition, int pYPosition, String pText, CallbackInfo callback) {
        if(pStack.getItem() instanceof NitroCanisterItem) {
            int width = pStack.getBarWidth();

            BufferBuilder buffer = Tesselator.getInstance().getBuilder();
            RenderSystem.disableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.disableBlend();
            fillRect(buffer, pXPosition + 2, pYPosition + 13, 13, 2, 0, 0, 0, 255);
            RenderSystem.enableBlend();
            RenderSystem.enableTexture();
            RenderSystem.enableDepthTest();

            RenderingUtils.drawGradientRect(pXPosition + 2, pYPosition + 13, width, 1, 0xFFFF0000, 0xFF0000FF);
        }
    }
}
