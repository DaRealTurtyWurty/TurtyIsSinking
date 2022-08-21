package dev.turtywurty.turtyissinking.init;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.blockentities.BackpackBlockEntity;
import dev.turtywurty.turtyissinking.blockentities.PlayerBoneBlockEntity;
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
}
