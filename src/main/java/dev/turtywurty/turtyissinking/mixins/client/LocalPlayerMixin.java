package dev.turtywurty.turtyissinking.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import dev.turtywurty.turtyissinking.entities.Wheelchair;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.Level;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends Player {
    @Shadow
    private Input input;

    @Shadow
    private boolean handsBusy;

    private LocalPlayerMixin(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile,
        ProfilePublicKey pProfilePublicKey) {
        super(pLevel, pPos, pYRot, pGameProfile, pProfilePublicKey);
    }

    @Inject(at = @At("TAIL"), method = "rideTick")
    private void turtyissinking$rideTick(CallbackInfo callback) {
        if (getVehicle() instanceof final Wheelchair wheelchair) {
            // wheelchair.setInput(this.input.left, this.input.right, this.input.up, this.input.down);
            this.handsBusy |= this.input.left || this.input.right || this.input.up || this.input.down;
        }
    }
}
