package dev.turtywurty.turtyissinking.networking;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.networking.clientbound.CSyncPlayerBonesPacket;
import dev.turtywurty.turtyissinking.networking.serverbound.SOpenBackpackPacket;
import dev.turtywurty.turtyissinking.networking.serverbound.SSawBonePacket;
import dev.turtywurty.turtyissinking.networking.serverbound.SWheelchairBoostPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TurtyIsSinking.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);
    
    public static void init() {
        int index = 0;
        INSTANCE.messageBuilder(SOpenBackpackPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SOpenBackpackPacket::encode)
                .decoder(SOpenBackpackPacket::new)
                .consumerMainThread(SOpenBackpackPacket::handle)
                .add();

        INSTANCE.messageBuilder(SSawBonePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SSawBonePacket::encode)
                .decoder(SSawBonePacket::new)
                .consumerMainThread(SSawBonePacket::handle)
                .add();

        INSTANCE.messageBuilder(CSyncPlayerBonesPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CSyncPlayerBonesPacket::encode)
                .decoder(CSyncPlayerBonesPacket::new)
                .consumerMainThread(CSyncPlayerBonesPacket::handle)
                .add();

        INSTANCE.messageBuilder(SWheelchairBoostPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SWheelchairBoostPacket::encode)
                .decoder(SWheelchairBoostPacket::new)
                .consumerMainThread(SWheelchairBoostPacket::handle)
                .add();

        TurtyIsSinking.LOGGER.info("Registered {} packets for mod '{}'", index, TurtyIsSinking.MODID);
    }
}
