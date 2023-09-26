package dev.turtywurty.turtyissinking.entities;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.items.NitroCanisterItem;
import dev.turtywurty.turtyissinking.menus.WheelchairMenu;
import dev.turtywurty.turtyissinking.networking.PacketHandler;
import dev.turtywurty.turtyissinking.networking.clientbound.CSyncWheelchairInventoryPacket;
import dev.turtywurty.turtyissinking.networking.serverbound.SWheelchairBoostPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Wheelchair extends LivingEntity implements PlayerRideable, MenuProvider, IEntityAdditionalSpawnData {
    private static final Component CONTAINER_NAME =
            Component.translatable("container." + TurtyIsSinking.MODID + ".wheelchair");

    private final ItemStackHandler inventory = new ItemStackHandler(2);
    private final LazyOptional<ItemStackHandler> optional = LazyOptional.of(() -> this.inventory);

    private static final EntityDataAccessor<Boolean> IS_BOOSTING =
            SynchedEntityData.defineId(Wheelchair.class, EntityDataSerializers.BOOLEAN);

    public Wheelchair(EntityType<Wheelchair> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeItem(this.inventory.getStackInSlot(0));
        buffer.writeItem(this.inventory.getStackInSlot(1));
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.inventory.setStackInSlot(0, additionalData.readItem());
        this.inventory.setStackInSlot(1, additionalData.readItem());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_BOOSTING, false);
    }

    public boolean isBoosting() {
        return this.entityData.get(IS_BOOSTING);
    }

    public void setBoosting(boolean boosting) {
        this.entityData.set(IS_BOOSTING, boosting);
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return List.of();
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        final Entity entity = getFirstPassenger();
        if (entity instanceof final LivingEntity living)
            return living;

        return null;
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
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
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        if (pSource.getEntity() instanceof Player || pSource.isBypassInvul())
            remove(RemovalReason.KILLED);

        return false;
    }

    @Override
    public @NotNull InteractionResult interact(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        if (!this.level.isClientSide() && pPlayer.isShiftKeyDown()) {
            NetworkHooks.openScreen((ServerPlayer) pPlayer, this, this::writeMenuData);
            return InteractionResult.SUCCESS;
        }

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
    public void positionRider(@NotNull Entity pPassenger) {
        super.positionRider(pPassenger);
        if (pPassenger instanceof final Mob mob) {
            this.yBodyRot = mob.yBodyRot;
        }
    }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot pSlot, @NotNull ItemStack pStack) {
    }

    @Override
    public boolean showVehicleHealth() {
        return false;
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
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
                    float speed = (float) getAttributeValue(Attributes.MOVEMENT_SPEED);

                    boolean isUsingNitro = isBoosting() && hasNitro() && getDeltaMovement().x != 0 && getDeltaMovement().z != 0;
                    if (isUsingNitro) {
                        speed *= 5F;
                        PacketHandler.INSTANCE.sendToServer(new SWheelchairBoostPacket(getId()));
                    }

                    setSpeed(speed);
                    super.travel(new Vec3(f, pTravelVector.y, f1));

                    if (isUsingNitro) {
                        spawnNitroParticles();
                    }
                } else if (rider instanceof Player) {
                    setDeltaMovement(Vec3.ZERO);
                }

                calculateEntityAnimation(this, false);
                tryCheckInsideBlocks();
            } else {
                this.flyingSpeed = 0.02F;
                super.travel(pTravelVector);
            }
        }
    }

    public void boostFling() {
        if (getRandom().nextInt(100) == 0) {
            Entity entity = getControllingPassenger();
            if (entity != null) {
                entity.stopRiding();

                Vec3 force = entity.getLookAngle().multiply(100, 10, 100);
                entity.push(force.x, Math.abs(force.y), force.z);
                entity.hurtMarked = true;
            }
        }
    }

    @Override
    public double getAttributeValue(@NotNull Attribute pAttribute) {
        if (pAttribute == Attributes.MOVEMENT_SPEED)
            return 0.2D;

        return super.getAttributeValue(pAttribute);
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {
    }

    @Override
    protected boolean canAddPassenger(@NotNull Entity pPassenger) {
        return pPassenger instanceof Villager || pPassenger instanceof Zombie || pPassenger instanceof Player;
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        return capability == ForgeCapabilities.ITEM_HANDLER ? this.optional.cast() : super.getCapability(capability, facing);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.put("inventory", this.inventory.serializeNBT());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.inventory.deserializeNBT(pCompound.getCompound("inventory"));
    }

    @Override
    public @NotNull Component getName() {
        return CONTAINER_NAME;
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new WheelchairMenu(pContainerId, pPlayerInventory, this);
    }

    public LazyOptional<ItemStackHandler> getOptional() {
        return this.optional;
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    private void writeMenuData(FriendlyByteBuf buf) {
        buf.writeVarInt(getId());
    }

    public ItemStack getStackInSlot(int slot) {
        return this.inventory.getStackInSlot(slot);
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        this.inventory.setStackInSlot(slot, stack);
    }

    public boolean hasNitro() {
        ItemStack stack0 = getStackInSlot(0);
        CompoundTag tag0 = stack0.getOrCreateTagElement(TurtyIsSinking.MODID);
        if (tag0.contains("nitro", Tag.TAG_INT) && NitroCanisterItem.getNitro(stack0) > 0)
            return true;

        ItemStack stack1 = getStackInSlot(1);
        CompoundTag tag1 = stack1.getOrCreateTagElement(TurtyIsSinking.MODID);
        return tag1.contains("nitro", Tag.TAG_INT) && NitroCanisterItem.getNitro(stack1) > 0;
    }

    public void consumeNitro() {
        ItemStack stack = getStackInSlot(0);
        CompoundTag tag = stack.getOrCreateTagElement(TurtyIsSinking.MODID);
        int nitro = NitroCanisterItem.getNitro(stack);
        if (nitro > 0) {
            nitro--;
            tag.putInt("nitro", nitro);
            this.inventory.setStackInSlot(0, stack);
            updateInventory();

            boostFling();

            return;
        }

        stack = getStackInSlot(1);
        tag = stack.getOrCreateTagElement(TurtyIsSinking.MODID);
        nitro = NitroCanisterItem.getNitro(stack);
        if (nitro > 0) {
            nitro--;
            tag.putInt("nitro", nitro);
            this.inventory.setStackInSlot(1, stack);
            updateInventory();

            boostFling();
        }
    }

    private void spawnNitroParticles() {
        for (int i = 0; i < 10; i++) {
            double x = this.xo + (0.5D - random.nextDouble());
            double y = this.yo + 0.25D;
            double z = this.zo + (0.5D - random.nextDouble());

            this.level.addParticle(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    x,
                    y,
                    z,
                    0.5 - random.nextDouble(),
                    0.5 - random.nextDouble(),
                    0.5 - random.nextDouble());
        }
    }

    public int getNitro() {
        int nitro = 0;
        ItemStack stack = getStackInSlot(0);
        CompoundTag tag = stack.getOrCreateTagElement(TurtyIsSinking.MODID);
        if (tag.contains("nitro", Tag.TAG_INT))
            nitro += NitroCanisterItem.getNitro(stack);

        stack = getStackInSlot(1);
        tag = stack.getOrCreateTagElement(TurtyIsSinking.MODID);
        if (tag.contains("nitro", Tag.TAG_INT))
            nitro += NitroCanisterItem.getNitro(stack);

        return nitro;
    }

    public int getMaxNitro() {
        int maxNitro = 0;
        if (!getStackInSlot(0).isEmpty())
            maxNitro += 100;

        if (!getStackInSlot(1).isEmpty())
            maxNitro += 100;

        return maxNitro;
    }

    public void updateInventory() {
        PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this),
                new CSyncWheelchairInventoryPacket(getId(), getStackInSlot(0), getStackInSlot(1)));
    }
}
