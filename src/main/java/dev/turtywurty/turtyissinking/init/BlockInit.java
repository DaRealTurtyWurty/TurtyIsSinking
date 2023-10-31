package dev.turtywurty.turtyissinking.init;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.blocks.*;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
        TurtyIsSinking.MOD_ID);
    
    public static final RegistryObject<BackpackBlock> BACKPACK = BLOCKS.register("backpack", () -> new BackpackBlock(
        BlockBehaviour.Properties.of(Material.CLOTH_DECORATION).dynamicShape().noOcclusion().strength(2.5f, 10f)));

    public static final RegistryObject<ExpOreBlock> EXP_ORE = BLOCKS.register("exp_ore",
        () -> new ExpOreBlock(BlockBehaviour.Properties.copy(Blocks.COAL_ORE)));
    
    public static final RegistryObject<PlayerBoneBlock> PLAYER_LEFT_ARM = BLOCKS.register("player_left_arm",
        () -> new PlayerBoneBlock(BlockBehaviour.Properties.of(Material.EGG).dynamicShape().noOcclusion(),
            PlayerBone.LEFT_ARM));
    public static final RegistryObject<PlayerBoneBlock> PLAYER_RIGHT_ARM = BLOCKS.register("player_right_arm",
        () -> new PlayerBoneBlock(BlockBehaviour.Properties.of(Material.EGG).dynamicShape().noOcclusion(),
            PlayerBone.RIGHT_ARM));
    public static final RegistryObject<PlayerBoneBlock> PLAYER_LEFT_LEG = BLOCKS.register("player_left_leg",
        () -> new PlayerBoneBlock(BlockBehaviour.Properties.of(Material.EGG).dynamicShape().noOcclusion(),
            PlayerBone.LEFT_LEG));
    public static final RegistryObject<PlayerBoneBlock> PLAYER_RIGHT_LEG = BLOCKS.register("player_right_leg",
        () -> new PlayerBoneBlock(BlockBehaviour.Properties.of(Material.EGG).dynamicShape().noOcclusion(),
            PlayerBone.RIGHT_LEG));
    public static final RegistryObject<PlayerBoneBlock> PLAYER_BODY = BLOCKS.register("player_body",
        () -> new PlayerBoneBlock(BlockBehaviour.Properties.of(Material.EGG).dynamicShape().noOcclusion(),
            PlayerBone.BODY));
    
//    public static final RegistryObject<QuarryBlock> QUARRY = BLOCKS.register("quarry", () -> new QuarryBlock(
//        BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F)));

    public static final RegistryObject<PianoBlock> PIANO = BLOCKS.register("piano", () -> new PianoBlock(
        BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(5f).noOcclusion()));

    public static final RegistryObject<ClaymoreBlock> CLAYMORE = BLOCKS.register("claymore", () -> new ClaymoreBlock(
        BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F)));
}
