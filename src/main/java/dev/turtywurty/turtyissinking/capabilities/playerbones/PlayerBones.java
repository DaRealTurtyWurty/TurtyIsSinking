package dev.turtywurty.turtyissinking.capabilities.playerbones;

import java.util.List;

import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public interface PlayerBones extends INBTSerializable<ListTag> {
    List<PlayerBone> getSawn();

    void reset();

    void saw(Player player, PlayerBone bone);

    void setSawn(PlayerBone... bones);

    void unsaw(PlayerBone bone);
}
