package dev.turtywurty.turtyissinking.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBones;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBonesCapability;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(PlayerItemInHandLayer.class)
public class PlayerItemInHandLayerMixin {
    @Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
    private void turtyissinking$renderArmWithItem(LivingEntity pLivingEntity, ItemStack pItemStack,
        ItemTransforms.TransformType pTransformType, HumanoidArm pArm, PoseStack pPoseStack, MultiBufferSource pBuffer,
        int pPackedLight, CallbackInfo callback) {
        final Player player = (Player) pLivingEntity;
        final PlayerBones cap = player.getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
        if (cap == null)
            return;
        
        if (cap.getSawn().contains(PlayerBone.LEFT_ARM) && cap.getSawn().contains(PlayerBone.RIGHT_ARM)) {
            callback.cancel();
        }
    }
}
