package dev.turtywurty.turtyissinking.menus;

import org.jetbrains.annotations.NotNull;

import dev.turtywurty.turtyissinking.blockentities.BackpackBlockEntity;
import dev.turtywurty.turtyissinking.init.BlockInit;
import dev.turtywurty.turtyissinking.init.ItemInit;
import dev.turtywurty.turtyissinking.init.MenuInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BackpackMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess containerAccess;
    private final BlockPos pos;
    private final ItemStackHandler inventory;
    
    private int backpackSlot = -1;
    
    public BackpackMenu(int id, Inventory playerInv) {
        this(id, playerInv, new ItemStackHandler(27), BlockPos.ZERO);
    }
    
    public BackpackMenu(int id, Inventory playerInv, IItemHandler slots, BlockPos pos) {
        super(MenuInit.BACKPACK.get(), id);
        this.containerAccess = ContainerLevelAccess.create(playerInv.player.level, pos);
        this.pos = pos;
        this.inventory = (ItemStackHandler) slots;
        
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new SlotItemHandler(slots, row * 9 + column, 8 + column * 18, 18 + row * 18) {
                    @Override
                    public boolean mayPlace(@NotNull ItemStack stack) {
                        return super.mayPlace(stack) && !stack.is(ItemInit.BACKPACK.get());
                    }
                });
            }
        }
        
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInv, 9 + row * 9 + column, 8 + column * 18, 84 + row * 18));
            }
        }
        
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInv, column, 8 + column * 18, 142));
        }
    }
    
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        var retStack = ItemStack.EMPTY;
        final Slot slot = getSlot(index);
        if (slot.hasItem()) {
            final ItemStack item = slot.getItem();
            retStack = item.copy();
            if (index < 27) {
                if (!moveItemStackTo(item, 27, this.slots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!moveItemStackTo(item, 0, 27, false))
                return ItemStack.EMPTY;
            
            if (item.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        
        return retStack;
    }
    
    @Override
    public void removed(Player player) {
        super.removed(player);
        if (this.backpackSlot != -1) {
            final ItemStack backpack = player.getInventory().getItem(this.backpackSlot);
            backpack.getOrCreateTag().put("Inventory", this.inventory.serializeNBT());
            player.getInventory().setItem(this.backpackSlot, backpack);
        }
    }
    
    public void setBackpackSlot(int slot) {
        this.backpackSlot = slot;
    }
    
    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.containerAccess, player, BlockInit.BACKPACK.get())
            || player.getMainHandItem().getItem() == ItemInit.BACKPACK.get()
            || player.getItemBySlot(EquipmentSlot.CHEST).getItem() == ItemInit.BACKPACK.get();
    }
    
    public static BackpackMenu getClientContainer(int id, Inventory playerInv) {
        return new BackpackMenu(id, playerInv, new ItemStackHandler(27), BlockPos.ZERO);
    }
    
    public static MenuConstructor getServerContainerProvider(BackpackBlockEntity blockEntity, BlockPos activationPos) {
        return getServerContainerProvider(blockEntity.getInventory(), activationPos);
    }
    
    public static MenuConstructor getServerContainerProvider(IItemHandler inventory, BlockPos activationPos) {
        return (id, playerInv, player) -> new BackpackMenu(id, playerInv, inventory, activationPos);
    }

    public static MenuConstructor getServerContainerProvider(IItemHandler inventory, BlockPos activationPos,
        int backpackSlot) {
        return (id, playerInv, player) -> {
            final var menu = new BackpackMenu(id, playerInv, inventory, activationPos);
            menu.setBackpackSlot(backpackSlot);
            return menu;
        };
    }
}
