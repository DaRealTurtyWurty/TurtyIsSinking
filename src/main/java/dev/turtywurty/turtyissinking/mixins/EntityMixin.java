package dev.turtywurty.turtyissinking.mixins;

import com.mojang.authlib.GameProfile;
import dev.turtywurty.turtyissinking.blockentities.PlayerBoneBlockEntity;
import dev.turtywurty.turtyissinking.blocks.PlayerBoneBlock;
import dev.turtywurty.turtyissinking.client.particles.PlayerSkinParticleOption;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(
            method = "spawnSprintParticle",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void turtyissinking$spawnSprintParticle(CallbackInfo callback, int x, int y, int z,
                                                    BlockPos pos, BlockState state, Vec3 movement) {
        Entity entity = (Entity) (Object) this;
        Level level = entity.level;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity instanceof PlayerBoneBlockEntity be) {
            GameProfile profile = be.getGameProfile();
            BonesawScreen.PlayerBone bone = ((PlayerBoneBlock)be.getBlockState().getBlock()).getBone();
            RandomSource random = level.getRandom();
            level.addParticle(new PlayerSkinParticleOption(profile, bone), entity.getX() + (random.nextDouble() - 0.5D) * (double)entity.getBbWidth(), entity.getY() + 0.1D, entity.getZ() + (random.nextDouble() - 0.5D) * (double)entity.getBbWidth(), movement.x * -4.0D, 1.5D, movement.z * -4.0D);
            callback.cancel();
        }
    }
}
