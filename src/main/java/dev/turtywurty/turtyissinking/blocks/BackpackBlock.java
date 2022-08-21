package dev.turtywurty.turtyissinking.blocks;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import dev.turtywurty.turtyissinking.blockentities.BackpackBlockEntity;
import dev.turtywurty.turtyissinking.init.BlockEntityInit;
import dev.turtywurty.turtyissinking.menus.BackpackMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.network.NetworkHooks;

public class BackpackBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final Map<Direction, VoxelShape> VOXEL_SHAPES = new EnumMap<>(Direction.class);
    private static final VoxelShape VOXEL_SHAPE = makeShape();

    public BackpackBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
        runCalculation(VOXEL_SHAPE);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        final BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof final BackpackBlockEntity backpack) {
            final Container container = new RecipeWrapper(backpack.getInventory());
            return AbstractContainerMenu.getRedstoneSignalFromContainer(container);
        }

        return super.getAnalogOutputSignal(state, level, pos);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos,
        Player player) {
        final ItemStack superStack = super.getCloneItemStack(state, target, level, pos, player);
        if (level.getBlockEntity(pos) instanceof final BackpackBlockEntity backpack) {
            superStack.getOrCreateTag().put("Inventory", backpack.getInventory().serializeNBT());
        } else {
            superStack.getOrCreateTag().put("Inventory", new ItemStackHandler(0).serializeNBT());
        }

        return superStack;
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        final BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof final BackpackBlockEntity backpack) {
            builder = builder.withDynamicDrop(new ResourceLocation("Inventory"), (ctx, stack) -> {
                for (int index = 0; index < backpack.getInventory().getSlots(); index++) {
                    stack.accept(backpack.getInventory().getStackInSlot(index));
                }
            });
        }

        return super.getDrops(state, builder);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return VOXEL_SHAPES.get(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityInit.BACKPACK.get().create(pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            level.getBlockEntity(pos, BlockEntityInit.BACKPACK.get())
                .ifPresent($ -> level.updateNeighbourForOutputSignal(pos, state.getBlock()));

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level.getBlockEntity(pos) instanceof final BackpackBlockEntity backpack && !level.isClientSide) {
            final ItemStack stack = asItem().getDefaultInstance();
            stack.getOrCreateTag().put("Inventory", backpack.getInventory().serializeNBT());

            final var itemEntity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                stack);
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof final BackpackBlockEntity backpack
            && stack.getOrCreateTag().contains("Inventory")) {
            backpack.getInventory().deserializeNBT(stack.getTag().getCompound("Inventory"));
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
        BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof final BackpackBlockEntity be) {
            final MenuConstructor containerProvider = BackpackMenu.getServerContainerProvider(be, pos);
            final MenuProvider provider = new SimpleMenuProvider(containerProvider, Component.empty());
            NetworkHooks.openScreen((ServerPlayer) player, provider, player.blockPosition());
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    protected void runCalculation(VoxelShape shape) {
        for (final Direction direction : Direction.values()) {
            VOXEL_SHAPES.put(direction, calculateShapes(direction, shape));
        }
    }

    public static VoxelShape calculateShapes(Direction to, VoxelShape shape) {
        final VoxelShape[] buffer = { shape, Shapes.empty() };

        final int times = (to.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY,
                maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }

    private static VoxelShape makeShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.34375, 0.75, 0.25, 0.59375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.25, 0.40625, 0.6875, 0.375, 0.59375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.375, 0.375, 0.46875, 0.625, 0.5, 0.59375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0, 0.59375, 0.6875, 0.5625, 0.65625), BooleanOp.OR);
        return Shapes.join(shape, Shapes.box(0.375, 0.5625, 0.59375, 0.625, 0.625, 0.65625), BooleanOp.OR);
    }
}
