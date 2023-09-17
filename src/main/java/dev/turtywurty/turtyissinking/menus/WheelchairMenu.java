package dev.turtywurty.turtyissinking.menus;

import dev.turtywurty.turtyissinking.entities.Wheelchair;
import dev.turtywurty.turtyissinking.init.ItemInit;
import dev.turtywurty.turtyissinking.init.MenuInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class WheelchairMenu extends AbstractContainerMenu {
    private final Wheelchair entity;

    // Client Constructor
    public WheelchairMenu(int pContainerId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(pContainerId, playerInventory, (Wheelchair) playerInventory.player.level.getEntity(buf.readVarInt()));
    }

    // Server Constructor
    public WheelchairMenu(int pContainerId, Inventory playerInventory, Wheelchair entity) {
        super(MenuInit.WHEELCHAIR.get(), pContainerId);

        this.entity = entity;

        createPlayerHotbar(playerInventory);
        createPlayerInventory(playerInventory);
        createEntityInventory();
    }

    private void createPlayerInventory(Inventory inv) {
        for(int row = 0; row < 3; row++) {
            for(int column = 0; column < 9; column++) {
                addSlot(new Slot(inv, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
    }

    private void createPlayerHotbar(Inventory inv) {
        for(int column = 0; column < 9; column++) {
            addSlot(new Slot(inv, column, 8 + column * 18, 142));
        }
    }

    private void createEntityInventory() {
        this.entity.getOptional().ifPresent(inventory -> {
            addSlot(new NitroCanisterSlot(inventory, 0, 44, 36));
            addSlot(new NitroCanisterSlot(inventory, 1, 116, 36));
        });
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        Slot fromSlot = getSlot(pIndex);
        ItemStack fromStack = fromSlot.getItem();

        if(fromStack.getCount() <= 0)
            fromSlot.set(ItemStack.EMPTY);

        if(!fromSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack toStack = fromStack.copy();
        if(pIndex < 36) {
            if(!moveItemStackTo(fromStack, 36, 38, false))
                return ItemStack.EMPTY;
        } else if(pIndex < 38) {
            if(!moveItemStackTo(fromStack, 0, 36, false))
                return ItemStack.EMPTY;
        } else {
            System.err.println("Invalid slot index: " + pIndex);
            return ItemStack.EMPTY;
        }

        fromSlot.setChanged();
        fromSlot.onTake(pPlayer, fromStack);
        return toStack;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return pPlayer.distanceToSqr(this.entity) <= 64;
    }

    public static class NitroCanisterSlot extends SlotItemHandler {
        public NitroCanisterSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public int getMaxStackSize(@NotNull ItemStack stack) {
            return 1;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return stack.is(ItemInit.NITRO_CANISTER.get()) && super.mayPlace(stack);
        }
    }
}
