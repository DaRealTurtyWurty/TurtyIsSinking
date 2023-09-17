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
    private final float speed;
    private final boolean isForwardKeyDown, isBackwardKeyDown, isLeftKeyDown, isRightKeyDown;

    public SWheelchairBoostPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readFloat(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    public SWheelchairBoostPacket(int entityID, float speed, boolean isForwardKeyDown, boolean isBackwardKeyDown, boolean isLeftKeyDown, boolean isRightKeyDown) {
        this.entityID = entityID;
        this.speed = speed;

        this.isForwardKeyDown = isForwardKeyDown;
        this.isBackwardKeyDown = isBackwardKeyDown;
        this.isLeftKeyDown = isLeftKeyDown;
        this.isRightKeyDown = isRightKeyDown;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeFloat(this.speed);
        buf.writeBoolean(this.isForwardKeyDown);
        buf.writeBoolean(this.isBackwardKeyDown);
        buf.writeBoolean(this.isLeftKeyDown);
        buf.writeBoolean(this.isRightKeyDown);
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
                wheelchair.setSpeed(this.speed);
                wheelchair.setForwardKeyDown(this.isForwardKeyDown);
                wheelchair.setBackwardKeyDown(this.isBackwardKeyDown);
                wheelchair.setLeftKeyDown(this.isLeftKeyDown);
                wheelchair.setRightKeyDown(this.isRightKeyDown);
                wheelchair.serverMovement();
            }
        });

        context.get().setPacketHandled(true);
    }
}
