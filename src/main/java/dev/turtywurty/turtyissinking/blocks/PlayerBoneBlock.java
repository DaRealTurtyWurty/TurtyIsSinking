package dev.turtywurty.turtyissinking.blocks;

import com.mojang.authlib.GameProfile;
import dev.turtywurty.turtyissinking.blockentities.PlayerBoneBlockEntity;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import dev.turtywurty.turtyissinking.init.BlockEntityInit;
import dev.turtywurty.turtyissinking.items.PlayerBoneItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class PlayerBoneBlock extends Block implements EntityBlock {
    private static final Map<PlayerBone, Block> BONE_MAP = new EnumMap<>(PlayerBone.class);;
    private static final Map<PlayerBone, VoxelShape> SHAPE_MAP = new EnumMap<>(PlayerBone.class);
    
    static {
        BONE_MAP.put(PlayerBone.HEAD, Blocks.PLAYER_HEAD);

        SHAPE_MAP.put(PlayerBone.LEFT_ARM, Block.box(6, 0, 6, 10, 12, 10));
        SHAPE_MAP.put(PlayerBone.RIGHT_ARM, Block.box(6, 0, 6, 10, 12, 10));
        SHAPE_MAP.put(PlayerBone.LEFT_LEG, Block.box(6, 0, 6, 10, 12, 10));
        SHAPE_MAP.put(PlayerBone.RIGHT_LEG, Block.box(6, 0, 6, 10, 12, 10));
        SHAPE_MAP.put(PlayerBone.BODY, Block.box(4, 0, 6, 12, 12, 10));
        SHAPE_MAP.put(PlayerBone.HEAD, Block.box(4, 0, 4, 12, 8, 12));
    }
    
    private final PlayerBone bone;
    
    public PlayerBoneBlock(Properties pProperties, PlayerBone bone) {
        super(pProperties);
        if (BONE_MAP.containsKey(bone))
            throw new IllegalArgumentException(bone.name() + " has already been registered as a PlayerBoneBlock");
        
        this.bone = bone;
        BONE_MAP.put(this.bone, this);
    }
    
    public PlayerBone getBone() {
        return this.bone;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE_MAP.getOrDefault(this.bone, super.getShape(pState, pLevel, pPos, pContext));
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return BlockEntityInit.PLAYER_BONE.get().create(pPos, pState);
    }

    @Override
    public void setPlacedBy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @Nullable LivingEntity pPlacer, @NotNull ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (pPlacer instanceof Player player
                && !pLevel.isClientSide
                && blockEntity instanceof PlayerBoneBlockEntity be) {
            be.setPlayer(player);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(!(blockEntity instanceof PlayerBoneBlockEntity be))
            return stack;

        GameProfile profile = be.getGameProfile();
        if (profile == null)
            PlayerBoneItem.setPlayer(stack, player);
        else
            PlayerBoneItem.setPlayer(stack, profile);

        return stack;
    }

    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState pState, LootContext.@NotNull Builder pBuilder) {
        List<ItemStack> drops = super.getDrops(pState, pBuilder);
        ItemStack bone = drops.stream().filter(stack -> stack.getItem() instanceof PlayerBoneItem).findFirst().orElse(ItemStack.EMPTY);
        if (bone.isEmpty())
            return drops;

        BlockEntity blockEntity = pBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (!(blockEntity instanceof PlayerBoneBlockEntity be)) {
            Entity entity = pBuilder.getOptionalParameter(LootContextParams.THIS_ENTITY);
            if (!(entity instanceof Player player))
                return drops;

            removeAndAdd(drops, bone, player.getGameProfile());
            return drops;
        }

        GameProfile profile = be.getGameProfile();
        if (profile == null) {
            Entity entity = pBuilder.getOptionalParameter(LootContextParams.THIS_ENTITY);
            if (!(entity instanceof Player player))
                return drops;

            removeAndAdd(drops, bone, player.getGameProfile());
            return drops;
        }

        removeAndAdd(drops, bone, profile);
        return drops;
    }

    private static void removeAndAdd(List<ItemStack> drops, ItemStack stack, GameProfile profile) {
        PlayerBoneItem.setPlayer(stack, profile);
        drops.removeIf(stack1 -> stack1.getItem() instanceof PlayerBoneItem);
        drops.add(stack);
    }

    public static Block blockByBone(PlayerBone bone) {
        return BONE_MAP.get(bone);
    }
    
    public static Item itemByBone(PlayerBone bone) {
        return blockByBone(bone).asItem();
    }
}
