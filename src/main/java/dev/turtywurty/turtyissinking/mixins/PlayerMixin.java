package dev.turtywurty.turtyissinking.mixins;

import org.spongepowered.asm.mixin.Mixin;

import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBones;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBonesCapability;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

// TODO: Fix this. Fuck mojang!
@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    private PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @Override
    public Pose getPose() {
        final PlayerBones cap = this.getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
        if (cap == null)
            return super.getPose();
        
        if (cap.getSawn().contains(PlayerBone.LEFT_LEG) && cap.getSawn().contains(PlayerBone.RIGHT_LEG))
            return Pose.STANDING;
        
        return super.getPose();
    }
}
