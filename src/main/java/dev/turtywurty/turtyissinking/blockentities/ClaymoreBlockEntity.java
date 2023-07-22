package dev.turtywurty.turtyissinking.blockentities;

import dev.turtywurty.turtyissinking.blockentities.base.TickableBlockEntity;
import dev.turtywurty.turtyissinking.blocks.ClaymoreBlock;
import dev.turtywurty.turtyissinking.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class ClaymoreBlockEntity extends BlockEntity implements TickableBlockEntity {
    private AABB detectionBox;

    public ClaymoreBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.CLAYMORE.get(), pPos, pBlockState);
    }

    @Override
    public void tick() {
        if(this.level == null || this.level.isClientSide) return;

        BlockPos position = getBlockPos();
        if (this.detectionBox == null) {
            VoxelShape shape = getBlockState().getShape(this.level, position, CollisionContext.empty());
            Direction facing = getBlockState().getValue(ClaymoreBlock.FACING);
            this.detectionBox = shape.bounds()
                    .move(position.getX(), position.getY(), position.getZ())
                    .move(facing.getStepX(), facing.getStepY(), facing.getStepZ());
        }

        // get all entities in the detection box
        List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, this.detectionBox, entity -> !entity.isDeadOrDying());

        // if there are entities, explode
        if(!entities.isEmpty()) {
            this.level.destroyBlock(position, false);
            this.level.explode(null, position.getX(), position.getY(), position.getZ(), 5.0F, true, Level.ExplosionInteraction.BLOCK);
        }
    }
}
