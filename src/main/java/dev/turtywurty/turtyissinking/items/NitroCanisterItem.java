package dev.turtywurty.turtyissinking.items;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NitroCanisterItem extends Item {
    private static final int MAX_NITRO = 100;

    public NitroCanisterItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack pStack) {
        return Math.round(13.0F * getNitroPercent(pStack));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel,
                                @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(
                Component.translatable("tooltip." + TurtyIsSinking.MOD_ID + ".nitro_canister")
                        .append(": " + (getNitroPercent(pStack) * 100) + "%"));
    }

    public static int getNitro(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTagElement(TurtyIsSinking.MOD_ID);
        if(!nbt.contains("nitro", Tag.TAG_INT))
            nbt.putInt("nitro", MAX_NITRO);

        return nbt.getInt("nitro");
    }

    public static float getNitroPercent(ItemStack stack) {
        return getNitro(stack) / (float) MAX_NITRO;
    }
}
