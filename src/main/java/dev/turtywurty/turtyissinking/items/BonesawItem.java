package dev.turtywurty.turtyissinking.items;

import dev.turtywurty.turtyissinking.client.screens.BonesawScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class BonesawItem extends Item {
    public BonesawItem(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> Minecraft.getInstance().setScreen(new BonesawScreen(pPlayer.getItemInHand(pUsedHand))));
        }

        return InteractionResultHolder.sidedSuccess(pPlayer.getItemInHand(pUsedHand), pLevel.isClientSide);
    }
}
