package dev.turtywurty.turtyissinking.blocks;

import dev.turtywurty.turtyissinking.blockentities.QuarryBlockEntity;
import dev.turtywurty.turtyissinking.blocks.base.TickingBlockEntityBlock;
import dev.turtywurty.turtyissinking.client.screens.QuarryScreen;
import dev.turtywurty.turtyissinking.init.BlockEntityInit;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class QuarryBlock extends TickingBlockEntityBlock<QuarryBlockEntity> {
    public QuarryBlock(Properties pProperties) {
        super(pProperties, BlockEntityInit.QUARRY);
    }
    
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
        BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> Minecraft.getInstance().setScreen(new QuarryScreen()));
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }
}
