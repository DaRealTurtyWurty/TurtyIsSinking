package dev.turtywurty.turtyissinking.capabilities.playerbones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class PlayerBonesCapability implements PlayerBones {
    public static final Capability<PlayerBones> PLAYER_BONES = CapabilityManager.get(new CapabilityToken<>() {
    });

    private List<PlayerBone> sawn = new ArrayList<>();
    
    @Override
    public void deserializeNBT(ListTag nbt) {
        setSawn(nbt.stream().map(Tag::getAsString).map(PlayerBone::valueOf).toArray(PlayerBone[]::new));
    }
    
    @Override
    public List<PlayerBone> getSawn() {
        return this.sawn;
    }

    @Override
    public void reset() {
        this.sawn.clear();
    }
    
    @Override
    public void saw(PlayerBone bone) {
        if (!this.sawn.contains(bone)) {
            this.sawn.add(bone);
        }
    }
    
    @Override
    public ListTag serializeNBT() {
        final var list = new ListTag();
        for (final PlayerBone bone : this.sawn) {
            list.add(StringTag.valueOf(bone.name()));
        }

        return list;
    }

    @Override
    public void setSawn(PlayerBone... bones) {
        if (this.sawn == null) {
            this.sawn = new ArrayList<>();
        }
        
        Collections.addAll(this.sawn, bones);
    }

    @Override
    public void unsaw(PlayerBone bone) {
        if (this.sawn.contains(bone)) {
            this.sawn.remove(bone);
        }
    }
}
