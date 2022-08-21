package dev.turtywurty.turtyissinking.items;

import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import dev.turtywurty.turtyissinking.client.renderers.bewlr.BackpackItemRenderer;
import dev.turtywurty.turtyissinking.init.BlockInit;
import dev.turtywurty.turtyissinking.init.ItemInit;
import dev.turtywurty.turtyissinking.networking.PacketHandler;
import dev.turtywurty.turtyissinking.networking.serverbound.SOpenBackpackPacket;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.items.ItemStackHandler;

public class BackpackItem extends BlockItem {
    public BackpackItem(Properties properties) {
        super(BlockInit.BACKPACK.get(), properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltips, flag);
        
        /*
         * final NonNullList<ItemStack> inventory = BackpackItem.getInventory(stack); int displayIndex = 0; int index =
         * 0; for (final ItemStack itemStack : inventory) { if (!itemStack.isEmpty()) { ++index; if (displayIndex <= 4)
         * { ++displayIndex; final MutableComponent mutablecomponent = itemStack.getHoverName().copy();
         * mutablecomponent.append(" x").append(String.valueOf(itemStack.getCount())); tooltips.add(mutablecomponent); }
         * } } if (index - displayIndex > 0) { tooltips.add(Component.translatable("container.shulkerBox.more", index -
         * displayIndex) .withStyle(ChatFormatting.ITALIC)); }
         */
    }
    
    @Override
    public @Nullable EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.CHEST;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return BackpackItemRenderer.INSTANCE;
            }
        });
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide && player.getItemInHand(hand).getItem() == ItemInit.BACKPACK.get()) {
            openBackpack(player.getInventory().findSlotMatchingItem(player.getItemInHand(hand)));
        }
        
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
    }
    
    public static NonNullList<ItemStack> getInventory(ItemStack backpack) {
        final var inventory = new ItemStackHandler(27);
        inventory.deserializeNBT(backpack.getOrCreateTag().getCompound("Inventory"));
        
        if (inventory.getSlots() > 0) {
            final NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
            for (int index = 0; index < inventory.getSlots() || index < 27; index++) {
                try {
                    items.add(index, inventory.getStackInSlot(index));
                } catch (final UnsupportedOperationException exception) {
                    
                }
            }

            System.out.println(items);

            return items;
        }
        
        return NonNullList.withSize(27, ItemStack.EMPTY);
    }

    public static void openBackpack(int slot) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
            () -> () -> PacketHandler.INSTANCE.sendToServer(new SOpenBackpackPacket(slot)));
    }
}
