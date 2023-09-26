package dev.turtywurty.turtyissinking.blockentities;

import com.mojang.authlib.GameProfile;
import dev.turtywurty.turtyissinking.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerBoneBlockEntity extends BlockEntity {
    private GameProfile gameProfile;

    public PlayerBoneBlockEntity(BlockPos pPos, BlockState pBlockState) {
        this(pPos, pBlockState, null);
    }

    public PlayerBoneBlockEntity(BlockPos pos, BlockState state, Player player) {
        super(BlockEntityInit.PLAYER_BONE.get(), pos, state);
        this.gameProfile = player == null ?
                new GameProfile(UUID.randomUUID(), "Steve") :
                player.getGameProfile();
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public void setPlayer(@Nullable Player player) {
        setGameProfile(player == null ? null : player.getGameProfile());
    }

    public void setGameProfile(@Nullable GameProfile gameProfile) {
        this.gameProfile = gameProfile == null ? this.gameProfile : gameProfile;

        if (this.level != null && !this.level.isClientSide) {
            setChanged();
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public void load(@NotNull CompoundTag pCompound) {
        super.load(pCompound);
        if (pCompound.contains("player", Tag.TAG_COMPOUND)) {
            this.gameProfile = NbtUtils.readGameProfile(pCompound.getCompound("player"));
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (this.gameProfile != null) {
            pTag.put("player", NbtUtils.writeGameProfile(new CompoundTag(), this.gameProfile));
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
