package dev.turtywurty.turtyissinking.blocks.base;

import java.util.function.Supplier;

import dev.turtywurty.turtyissinking.blockentities.base.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TickingBlockEntityBlock<T extends BlockEntity & TickableBlockEntity> extends Block implements EntityBlock {
    private final Supplier<BlockEntityType<T>> blockEntityType;

    public TickingBlockEntityBlock(Properties pProperties, Supplier<BlockEntityType<T>> blockEntityType) {
        super(pProperties);
        this.blockEntityType = blockEntityType;
    }

    @Override
    public <B extends BlockEntity> BlockEntityTicker<B> getTicker(Level pLevel, BlockState pState,
        BlockEntityType<B> pBlockEntityType) {
        return ($, pPos, $0, pBlockEntity) -> {
            if (pBlockEntity.getType().equals(this.blockEntityType.get())) {
                ((TickableBlockEntity) pBlockEntity).tick();
            }
        };
    }

    @Override
    public final BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return this.blockEntityType.get().create(pPos, pState);
    }
}
