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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class ClaymoreBlockEntity extends BlockEntity implements TickableBlockEntity {
    private AABB detectionBox;
    private int ticks = 0;

    public ClaymoreBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.CLAYMORE.get(), pPos, pBlockState);
    }

    @Override
    public void tick() {
        if(this.level == null || this.level.isClientSide())
            return;

        if (!getBlockState().getValue(ClaymoreBlock.ACTIVATED))
            return;

        BlockPos position = getBlockPos();
        if (this.detectionBox == null) {
            Direction facing = getBlockState().getValue(ClaymoreBlock.FACING);
            this.detectionBox = new AABB(position)
                    .move(facing.getStepX(), facing.getStepY(), facing.getStepZ());
        }

        if(this.ticks++ % 5 != 0)
            return;

        // get all entities in the detection box
        List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, this.detectionBox, entity -> entity.isAlive() && !entity.isSpectator());

        // if there are entities, explode
        if(!entities.isEmpty()) {
            explode(this.level, position);
        }
    }

    public static void explode(Level level, BlockPos position) {
        if (level == null || level.isClientSide)
            return;

        level.destroyBlock(position, false);
        level.explode(null, position.getX(), position.getY(), position.getZ(), 5.0F, true, Level.ExplosionInteraction.BLOCK);
    }
}
