package dev.turtywurty.turtyissinking.capabilities.playerbones;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public class PlayerBonesProvider implements ICapabilitySerializable<ListTag> {
    public static final ResourceLocation INDENTIFIER = new ResourceLocation(TurtyIsSinking.MODID, "player_bones");

    private final PlayerBones backend = new PlayerBonesCapability();
    private final LazyOptional<PlayerBones> optional = LazyOptional.of(() -> this.backend);

    @Override
    public void deserializeNBT(ListTag nbt) {
        this.backend.deserializeNBT(nbt);
    }
    
    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return PlayerBonesCapability.PLAYER_BONES.orEmpty(cap, this.optional);
    }
    
    public void invalidate() {
        this.optional.invalidate();
    }
    
    @Override
    public ListTag serializeNBT() {
        return this.backend.serializeNBT();
    }
    
    public static void attach(AttachCapabilitiesEvent<Entity> event) {
        final var provider = new PlayerBonesProvider();
        
        event.addCapability(INDENTIFIER, provider);
    }
}
