package dev.turtywurty.turtyissinking.mixins.client;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.turtywurty.turtyissinking.client.Materials;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;

@Mixin(Sheets.class)
public class SheetsMixin {
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/renderer/Sheets;getAllMaterials(Ljava/util/function/Consumer;)V")
    private static void turtyissinking$getAllMaterials(Consumer<Material> acceptor, CallbackInfo callback) {
        acceptor.accept(Materials.BACKPACK_LOCATION);
    }
}
