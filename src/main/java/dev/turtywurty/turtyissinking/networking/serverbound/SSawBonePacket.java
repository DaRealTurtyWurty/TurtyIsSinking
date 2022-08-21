package dev.turtywurty.turtyissinking.networking.serverbound;

import java.util.function.Supplier;

import dev.turtywurty.turtyissinking.blocks.PlayerBoneBlock;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBones;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBonesCapability;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class SSawBonePacket {
    private final PlayerBone bone;

    public SSawBonePacket(FriendlyByteBuf buf) {
        this(PlayerBone.valueOf(buf.readUtf()));
    }

    public SSawBonePacket(PlayerBone bone) {
        this.bone = bone;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(this.bone.name());
    }

    public static void handle(SSawBonePacket pkt, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            final Player player = context.get().getSender();
            player.swingingArm = InteractionHand.OFF_HAND;
            final PlayerBones cap = player.getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
            if (cap == null)
                throw new IllegalStateException("Player does not contain Player Bones capability!");
            cap.saw(player, pkt.bone);

            if (!player.isCreative() && (pkt.bone == PlayerBone.HEAD || pkt.bone == PlayerBone.BODY)) {
                player.kill();
                for (final PlayerBone bone : PlayerBone.values()) {
                    if (!cap.getSawn().contains(bone)) {
                        final ItemStack item = PlayerBoneBlock.itemByBone(bone).getDefaultInstance();
                        player.level.addFreshEntity(new ItemEntity(player.level, player.getX() + 0.5D,
                            player.getY() + 0.5D, player.getZ() + 0.5D, item));
                    }
                }
            }

            if (pkt.bone == PlayerBone.LEFT_ARM) {
                player.getEntityData().set(Player.DATA_PLAYER_MAIN_HAND, (byte) 1);
            } else if (pkt.bone == PlayerBone.RIGHT_ARM) {
                player.getEntityData().set(Player.DATA_PLAYER_MAIN_HAND, (byte) 0);
            }
        });
        context.get().setPacketHandled(true);
    }
}
