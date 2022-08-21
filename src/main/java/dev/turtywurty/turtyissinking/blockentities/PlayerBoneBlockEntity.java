package dev.turtywurty.turtyissinking.blockentities;

import dev.turtywurty.turtyissinking.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlayerBoneBlockEntity extends BlockEntity {
    private final Player player;

    public PlayerBoneBlockEntity(BlockPos pPos, BlockState pBlockState) {
        this(pPos, pBlockState, null);
    }

    public PlayerBoneBlockEntity(BlockPos pos, BlockState state, Player player) {
        super(BlockEntityInit.PLAYER_BONE.get(), pos, state);
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }
}
