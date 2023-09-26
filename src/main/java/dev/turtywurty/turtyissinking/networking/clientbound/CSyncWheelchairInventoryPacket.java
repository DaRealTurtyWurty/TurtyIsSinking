package dev.turtywurty.turtyissinking.networking.clientbound;

import dev.turtywurty.turtyissinking.entities.Wheelchair;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CSyncWheelchairInventoryPacket {
    private final int entityId;
    private final ItemStack stack0, stack1;

    public CSyncWheelchairInventoryPacket(int entityId, ItemStack stack0, ItemStack stack1) {
        this.entityId = entityId;
        this.stack0 = stack0;
        this.stack1 = stack1;
    }

    public CSyncWheelchairInventoryPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readItem(), buf.readItem());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeItem(this.stack0);
        buf.writeItem(this.stack1);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if(level == null)
                return;

            Entity entity = level.getEntity(this.entityId);
            if(entity == null)
                return;

            if(entity instanceof Wheelchair wheelchair) {
                wheelchair.setStackInSlot(0, this.stack0);
                wheelchair.setStackInSlot(1, this.stack1);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
