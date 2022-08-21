package dev.turtywurty.turtyissinking.entities;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PlayerRideable;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Wheelchair extends LivingEntity implements PlayerRideable {
    public Wheelchair(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return List.of();
    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        final Entity entity = getFirstPassenger();
        if (entity instanceof final LivingEntity living)
            return living;

        return null;
    }
    
    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }
    
    @Override
    public double getPassengersRidingOffset() {
        return 0.4D;
    }

    @Override
    public float getStepHeight() {
        return 1;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if (pPlayer.isSecondaryUseActive())
            return InteractionResult.PASS;

        if (!this.level.isClientSide)
            return pPlayer.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public boolean isVehicle() {
        return true;
    }

    @Override
    public void positionRider(Entity pPassenger) {
        super.positionRider(pPassenger);
        if (pPassenger instanceof final Mob mob) {
            this.yBodyRot = mob.yBodyRot;
        }
    }
    
    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
    }

    @Override
    public boolean showVehicleHealth() {
        return false;
    }
    
    @Override
    public void travel(Vec3 pTravelVector) {
        if (isAlive()) {
            final LivingEntity rider = getControllingPassenger();
            if (isVehicle() && rider != null) {
                setYRot(rider.getYRot());
                this.yRotO = getYRot();
                setXRot(rider.getXRot() * 0.5F);
                setRot(getYRot(), getXRot());
                this.yBodyRot = getYRot();
                this.yHeadRot = this.yBodyRot;
                final float f = rider.xxa * 0.5F;
                final float f1 = rider.zza;

                this.flyingSpeed = getSpeed() * 0.1F;
                if (isControlledByLocalInstance()) {
                    setSpeed((float) getAttributeValue(Attributes.MOVEMENT_SPEED));
                    super.travel(new Vec3(f, pTravelVector.y, f1));
                } else if (rider instanceof Player) {
                    this.setDeltaMovement(Vec3.ZERO);
                }

                calculateEntityAnimation(this, false);
                tryCheckInsideBlocks();
            } else {
                this.flyingSpeed = 0.02F;
                super.travel(pTravelVector);
            }
        }
    }
    
    @Override
    protected boolean canAddPassenger(Entity pPassenger) {
        return pPassenger instanceof Villager || pPassenger instanceof Zombie || pPassenger instanceof Player;
    }
    
    protected void doPlayerRide(Player pPlayer) {
        if (!this.level.isClientSide) {
            pPlayer.setYRot(getYRot());
            pPlayer.setXRot(getXRot());
            pPlayer.startRiding(this);
        }
    }
}
