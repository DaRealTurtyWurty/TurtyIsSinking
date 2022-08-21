package dev.turtywurty.turtyissinking.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ExpOreBlock extends Block {
    public ExpOreBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public int getExpDrop(BlockState state, LevelReader level, RandomSource randomSource, BlockPos pos,
        int fortuneLevel, int silkTouchLevel) {
        return (fortuneLevel + 1) * randomSource.nextInt(5, 10);
    }
}
