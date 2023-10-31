package dev.turtywurty.turtyissinking.networking.serverbound;

import dev.turtywurty.turtyissinking.networking.PacketHandler;
import dev.turtywurty.turtyissinking.networking.clientbound.CPianoKeyPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SPianoKeyPacket {
    private final BlockPos position;
    private final byte noteId;
    private final byte velocity;

    public SPianoKeyPacket(BlockPos position, byte noteId, byte velocity) {
        this.position = position;
        this.noteId = noteId;
        this.velocity = velocity;
    }

    public SPianoKeyPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readByte(), buf.readByte());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.position);
        buf.writeByte(this.noteId);
        buf.writeByte(this.velocity);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            context.setPacketHandled(true);
            PacketHandler.sendToAllClients(new CPianoKeyPacket(this.position, this.noteId, this.velocity));
        });
    }
}
