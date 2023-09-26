package dev.turtywurty.turtyissinking.mixins.client;

import dev.turtywurty.turtyissinking.client.ClientAccess;
import dev.turtywurty.turtyissinking.init.ItemInit;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {
    @Inject(method = "setup", at = @At("TAIL"))
    private void turtyissinking$moveCamera(BlockGetter pLevel, Entity pEntity, boolean pDetached, boolean pThirdPersonReverse, float pPartialTick, CallbackInfo callback) {
        Camera camera = (Camera) (Object) this;
        if(Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_FRONT &&
                Minecraft.getInstance().player.getMainHandItem().is(ItemInit.CAMERA.get())) {
            ClientAccess.adjustCamera(camera);
        }
    }
}
