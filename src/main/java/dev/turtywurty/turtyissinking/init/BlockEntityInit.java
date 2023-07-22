package dev.turtywurty.turtyissinking.init;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.blockentities.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
        .create(ForgeRegistries.BLOCK_ENTITY_TYPES, TurtyIsSinking.MODID);
    
    public static final RegistryObject<BlockEntityType<BackpackBlockEntity>> BACKPACK = BLOCK_ENTITIES.register(
        "backpack", () -> BlockEntityType.Builder.of(BackpackBlockEntity::new, BlockInit.BACKPACK.get()).build(null));

    public static final RegistryObject<BlockEntityType<PlayerBoneBlockEntity>> PLAYER_BONE = BLOCK_ENTITIES.register(
        "player_bone",
        () -> BlockEntityType.Builder
            .of(PlayerBoneBlockEntity::new, BlockInit.PLAYER_BODY.get(), BlockInit.PLAYER_LEFT_ARM.get(),
                BlockInit.PLAYER_LEFT_LEG.get(), BlockInit.PLAYER_RIGHT_ARM.get(), BlockInit.PLAYER_RIGHT_LEG.get())
            .build(null));

    public static final RegistryObject<BlockEntityType<QuarryBlockEntity>> QUARRY = BLOCK_ENTITIES.register("quarry",
        () -> BlockEntityType.Builder.of(QuarryBlockEntity::new, BlockInit.QUARRY.get()).build(null));

    public static final RegistryObject<BlockEntityType<PianoBlockEntity>> PIANO = BLOCK_ENTITIES.register("piano",
        () -> BlockEntityType.Builder.of(PianoBlockEntity::new, BlockInit.PIANO.get()).build(null));

    public static final RegistryObject<BlockEntityType<ClaymoreBlockEntity>> CLAYMORE = BLOCK_ENTITIES.register("claymore",
        () -> BlockEntityType.Builder.of(ClaymoreBlockEntity::new, BlockInit.CLAYMORE.get()).build(null));
}
