package dev.turtywurty.turtyissinking.blockentities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.turtywurty.turtyissinking.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class BackpackBlockEntity extends BlockEntity {
    private ItemStackHandler inventory;
    private final LazyOptional<ItemStackHandler> holder;

    public BackpackBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.BACKPACK.get(), pos, state);
        this.inventory = new ItemStackHandler(27) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                BlockEntity.setChanged(BackpackBlockEntity.this.level, pos, state);
            }
        };

        this.holder = LazyOptional.of(() -> this.inventory);
    }
    
    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? this.holder.cast()
            : super.getCapability(cap, side);
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }
    
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        final var update = new CompoundTag();
        saveAdditional(update);
        return update;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }
    
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.holder.invalidate();
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.inventory.deserializeNBT(nbt.getCompound("Inventory"));
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getTag());
    }
    
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("Inventory", this.inventory.serializeNBT());
    }
}
