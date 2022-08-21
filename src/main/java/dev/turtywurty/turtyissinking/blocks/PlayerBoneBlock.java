package dev.turtywurty.turtyissinking.blocks;

import java.util.EnumMap;
import java.util.Map;

import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import dev.turtywurty.turtyissinking.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlayerBoneBlock extends Block implements EntityBlock {
    private static final Map<PlayerBone, Block> BONE_MAP;
    
    static {
        BONE_MAP = new EnumMap<>(PlayerBone.class);
        BONE_MAP.put(PlayerBone.HEAD, Blocks.PLAYER_HEAD);
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
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityInit.PLAYER_BONE.get().create(pPos, pState);
    }
    
    public static Block blockByBone(PlayerBone bone) {
        return BONE_MAP.get(bone);
    }
    
    public static Item itemByBone(PlayerBone bone) {
        return blockByBone(bone).asItem();
    }
}
