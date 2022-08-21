package dev.turtywurty.turtyissinking.networking.clientbound;

import java.util.function.Supplier;
import java.util.stream.Stream;

import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBonesCapability;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class CSyncPlayerBonesPacket {
    private final PlayerBone[] bones;
    
    public CSyncPlayerBonesPacket(FriendlyByteBuf buf) {
        final String str = buf.readUtf();
        if (str.isBlank()) {
            this.bones = new PlayerBone[0];
            return;
        }
        
        final String[] found = str.split("\\|");
        if (found.length == 0) {
            this.bones = new PlayerBone[] { PlayerBone.valueOf(str) };
            return;
        }
        
        this.bones = Stream.of(found).map(PlayerBone::valueOf).toArray(PlayerBone[]::new);
    }
    
    public CSyncPlayerBonesPacket(PlayerBone... bones) {
        this.bones = bones;
    }
    
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(String.join("|", Stream.of(this.bones).map(PlayerBone::name).toArray(String[]::new)));
    }
    
    @SuppressWarnings("resource")
    public static void handle(CSyncPlayerBonesPacket pkt, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            final Player player = Minecraft.getInstance().player;
            player.getCapability(PlayerBonesCapability.PLAYER_BONES).ifPresent(cap -> cap.setSawn(pkt.bones));
        });
        context.get().setPacketHandled(true);
    }
}
