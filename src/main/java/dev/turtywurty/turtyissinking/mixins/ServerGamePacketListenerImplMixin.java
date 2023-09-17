package dev.turtywurty.turtyissinking.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBones;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBonesCapability;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Shadow
    public ServerPlayer player;

    //@formatter:off
    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"
        ),
        method = "handlePlayerAction",
        cancellable = true
    )
    //@formatter:on
    private void turtyissinking$handlePlayerAction(ServerboundPlayerActionPacket pPacket, CallbackInfo info) {
        if (pPacket.getAction() == Action.SWAP_ITEM_WITH_OFFHAND) {
            final PlayerBones cap = this.player.getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
            if (cap == null)
                return;

            if (cap.getSawn().contains(PlayerBone.LEFT_ARM) || cap.getSawn().contains(PlayerBone.RIGHT_ARM)) {
                info.cancel();
            }
        }
    }
}
