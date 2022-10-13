package dev.turtywurty.turtyissinking.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.ForgeEventFactory;

public class SledgehammerItem extends PickaxeItem {
    public SledgehammerItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos,
        LivingEntity pEntityLiving) {
        if (!(pEntityLiving instanceof final Player player))
            return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);

        final List<BlockPos> positions = getValidPositions(player);
        for (final BlockPos blockPos : positions) {
            pLevel.destroyBlock(blockPos, true, player);
        }

        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    public static List<BlockPos> getValidPositions(Player player) {
        return getValidPositions(player, Item.getPlayerPOVHitResult(player.level, player, Fluid.ANY));
    }

    public static List<BlockPos> getValidPositions(Player player, BlockHitResult hit) {
        if (player.isCreative() || player.isCrouching())
            return List.of();

        final List<BlockPos> positions = new ArrayList<>();
        final Direction direction = hit.getDirection();
        if (direction.getAxis().isHorizontal()) {
            final Direction left = direction.getCounterClockWise();
            final BlockPos topLeft = hit.getBlockPos().relative(left);

            for (int y = -1; y <= 1; y++) {
                for (int x = 0; x < 3; x++) {
                    final BlockPos position = topLeft.relative(left.getOpposite(), x).below(y);
                    if (position == hit.getBlockPos()) {
                        continue;
                    }

                    final boolean canDestroy = player.level.getBlockState(position).canEntityDestroy(player.level,
                        position, player)
                        && ForgeEventFactory.onEntityDestroyBlock(player, position,
                            player.level.getBlockState(position));
                    if (!canDestroy) {
                        continue;
                    }

                    positions.add(position);
                }
            }
        } else {
            for (int z = -1; z <= 1; z++) {
                for (int x = -1; x <= 1; x++) {
                    final BlockPos position = hit.getBlockPos().offset(x, 0, z);
                    if (position == hit.getBlockPos()) {
                        continue;
                    }

                    final boolean canDestroy = player.level.getBlockState(position).canEntityDestroy(player.level,
                        position, player)
                        && ForgeEventFactory.onEntityDestroyBlock(player, position,
                            player.level.getBlockState(position));
                    if (!canDestroy) {
                        continue;
                    }

                    positions.add(position);
                }
            }
        }

        return positions;
    }
}
