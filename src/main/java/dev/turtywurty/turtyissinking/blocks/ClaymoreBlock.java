package dev.turtywurty.turtyissinking.blocks;

import dev.turtywurty.turtyissinking.blockentities.ClaymoreBlockEntity;
import dev.turtywurty.turtyissinking.blockentities.base.TickableBlockEntity;
import dev.turtywurty.turtyissinking.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class ClaymoreBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    private static final VoxelShape N_SHAPE = Shapes.box(0.25, 0, 0.4375, 0.75, 0.5, 0.625);
    private static final VoxelShape E_SHAPE = Shapes.box(0.375, 0, 0.25, 0.5625, 0.5, 0.75);
    private static final VoxelShape S_SHAPE = Shapes.box(0.25, 0, 0.375, 0.75, 0.5, 0.5625);
    private static final VoxelShape W_SHAPE = Shapes.box(0.4375, 0, 0.25, 0.625, 0.5, 0.75);

    public ClaymoreBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ACTIVATED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING, ACTIVATED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection()).setValue(ACTIVATED, false);
    }

    @Override
    public @NotNull BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return switch (pState.getValue(FACING)) {
            case NORTH -> N_SHAPE;
            case EAST -> E_SHAPE;
            case SOUTH -> S_SHAPE;
            case WEST -> W_SHAPE;
            default -> Shapes.block();
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return BlockEntityInit.CLAYMORE.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : (pLevel1, pPos, pState1, pBlockEntity) -> ((TickableBlockEntity)pBlockEntity).tick();
    }

    @Override
    public boolean canSurvive(@NotNull BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.below()).isCollisionShapeFullBlock(pLevel, pPos.below());
    }

    @Override
    public void neighborChanged(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Block pBlock, @NotNull BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
        if (!pLevel.getBlockState(pPos.below()).isCollisionShapeFullBlock(pLevel, pPos.below())) {
            if (pState.getValue(ACTIVATED)) {
                ClaymoreBlockEntity.explode(pLevel, pPos);
            } else {
                pLevel.destroyBlock(pPos, true);
            }
        }
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(state, level, pos, explosion);
        ClaymoreBlockEntity.explode(level, pos);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if(pHand != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;

        if(pPlayer.isCrouching()) {
            if(!pLevel.isClientSide()) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(ACTIVATED, !pState.getValue(ACTIVATED)));
            }

            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        } else {
            return InteractionResult.PASS;
        }
    }
}
