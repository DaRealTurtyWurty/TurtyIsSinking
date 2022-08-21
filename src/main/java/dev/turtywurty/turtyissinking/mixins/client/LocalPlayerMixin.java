package dev.turtywurty.turtyissinking.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.authlib.GameProfile;

import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBones;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBonesCapability;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.Level;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends Player {
    @Shadow
    private boolean crouching;

    private LocalPlayerMixin(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile,
        ProfilePublicKey pProfilePublicKey) {
        super(pLevel, pPos, pYRot, pGameProfile, pProfilePublicKey);
    }
    
    @Override
    @Overwrite
    public boolean isCrouching() {
        final PlayerBones cap = getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
        if (cap == null)
            return this.crouching;

        if (cap.getSawn().contains(PlayerBone.LEFT_LEG) && cap.getSawn().contains(PlayerBone.RIGHT_LEG))
            return false;

        return this.crouching;
    }
}
