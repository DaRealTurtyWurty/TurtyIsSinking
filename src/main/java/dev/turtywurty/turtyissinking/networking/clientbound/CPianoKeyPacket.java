package dev.turtywurty.turtyissinking.networking.clientbound;

import dev.turtywurty.turtyissinking.blockentities.PianoBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import javax.sound.midi.MidiChannel;
import java.util.function.Supplier;

public class CPianoKeyPacket {
    private final BlockPos position;
    private final byte key;
    private final int volume;

    public CPianoKeyPacket(BlockPos position, byte key, int volume) {
        this.position = position;
        this.key = key;
        this.volume = volume;
    }

    public CPianoKeyPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readByte(), buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.position);
        buf.writeByte(this.key);
        buf.writeInt(this.volume);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            BlockPos pos = player.blockPosition();
            double distance = Math.sqrt(pos.distSqr(this.position));
            double volume = 1 - (distance / 10);
            volume *= this.volume;

            if(volume <= 0)
                return;

            int normalizedVolume = (int) (volume * 127);

            BlockEntity blockEntity = player.level.getBlockEntity(this.position);
            if(blockEntity instanceof PianoBlockEntity piano) {
                MidiChannel midi = piano.getMidi();
                midi.noteOn(this.key, normalizedVolume);
            }
        });
    }
}
