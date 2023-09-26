package dev.turtywurty.turtyissinking.networking.serverbound;

import dev.turtywurty.turtyissinking.entities.Wheelchair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SWheelchairBoostPacket {
    private final int entityID;

    public SWheelchairBoostPacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public SWheelchairBoostPacket(int entityID) {
        this.entityID = entityID;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer sender = context.get().getSender();
            if(sender == null)
                return;

            Level level = sender.getLevel();
            Entity entity = level.getEntity(this.entityID);
            if(entity instanceof Wheelchair wheelchair) {
                wheelchair.consumeNitro();
            }
        });

        context.get().setPacketHandled(true);
    }
}
