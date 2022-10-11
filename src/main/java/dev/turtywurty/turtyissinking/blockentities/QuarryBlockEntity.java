package dev.turtywurty.turtyissinking.blockentities;

import dev.turtywurty.turtyissinking.blockentities.base.SyncingBlockEntity;
import dev.turtywurty.turtyissinking.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class QuarryBlockEntity extends SyncingBlockEntity {
    private int width, height, depth;
    private int offsetX, offsetY, offsetZ;
    
    public QuarryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.QUARRY.get(), pPos, pBlockState);
    }
    
    @Override
    public void tick() {
        
    }
}
