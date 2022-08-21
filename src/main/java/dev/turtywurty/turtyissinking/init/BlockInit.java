package dev.turtywurty.turtyissinking.init;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.blocks.BackpackBlock;
import dev.turtywurty.turtyissinking.blocks.ExpOreBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
        TurtyIsSinking.MODID);
    
    public static final RegistryObject<BackpackBlock> BACKPACK = BLOCKS.register("backpack", () -> new BackpackBlock(
        BlockBehaviour.Properties.of(Material.CLOTH_DECORATION).dynamicShape().noOcclusion().strength(2.5f, 10f)));

    public static final RegistryObject<ExpOreBlock> EXP_ORE = BLOCKS.register("exp_ore",
        () -> new ExpOreBlock(BlockBehaviour.Properties.copy(Blocks.COAL_ORE)));
}
