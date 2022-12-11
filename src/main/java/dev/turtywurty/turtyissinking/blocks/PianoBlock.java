package dev.turtywurty.turtyissinking.blocks;

import dev.turtywurty.turtyissinking.blockentities.PianoBlockEntity;
import dev.turtywurty.turtyissinking.client.screens.PianoScreen;
import dev.turtywurty.turtyissinking.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class PianoBlock extends Block implements EntityBlock {
    public PianoBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public final BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityInit.PIANO.get().create(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
        BlockHitResult pHit) {
        if (pLevel.isClientSide() && pLevel.getBlockEntity(pPos) instanceof PianoBlockEntity) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> PianoScreen.open(pPos));
        }
        
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }
}
