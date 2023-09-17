package dev.turtywurty.turtyissinking.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBones;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBonesCapability;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.player.Player;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;attackIndicator()Lnet/minecraft/client/OptionInstance;"), method = "renderCrosshair", cancellable = true)
    private void turtyissinking$renderCrosshair(PoseStack pPoseStack, CallbackInfo callback) {
        final Player player = Minecraft.getInstance().player;
        final PlayerBones cap = player.getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
        if (cap == null)
            return;

        if (cap.getSawn().contains(PlayerBone.LEFT_ARM) && cap.getSawn().contains(PlayerBone.RIGHT_ARM)) {
            callback.cancel();
        }
    }
}
