package dev.turtywurty.turtyissinking.items;

import com.mojang.authlib.GameProfile;
import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.client.renderers.bewlr.PlayerBoneItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class PlayerBoneItem extends BlockItem {
    public PlayerBoneItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return PlayerBoneItemRenderer.INSTANCE;
            }
        });
    }

    public static void setPlayer(@NotNull ItemStack stack, @NotNull GameProfile profile) {
        CompoundTag tag = stack.getOrCreateTagElement(TurtyIsSinking.MODID);
        tag.put("Profile", NbtUtils.writeGameProfile(new CompoundTag(), profile));
    }

    public static void setPlayer(@NotNull ItemStack stack, @NotNull Player player) {
        setPlayer(stack, player.getGameProfile());
    }

    public static @Nullable GameProfile getPlayer(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(TurtyIsSinking.MODID);
        if (tag != null && tag.contains("Profile", Tag.TAG_COMPOUND)) {
            return NbtUtils.readGameProfile(tag.getCompound("Profile"));
        }

        return null;
    }

    public static boolean hasPlayer(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(TurtyIsSinking.MODID);
        return tag != null && tag.contains("Profile", Tag.TAG_COMPOUND);
    }
}
