package dev.turtywurty.turtyissinking.mixins;

import org.spongepowered.asm.mixin.Mixin;

import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBones;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBonesCapability;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

@Mixin(targets = "net.minecraft.world.inventory.InventoryMenu$2")
public class InventoryMenuMixin extends Slot {
    private InventoryMenuMixin(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }
    
    @Override
    public boolean mayPickup(Player pPlayer) {
        final Player player = ((Inventory) this.container).player;
        final PlayerBones cap = player.getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
        if (cap == null)
            return super.mayPickup(player);
        final boolean isAmputated = cap.getSawn().contains(PlayerBone.LEFT_ARM)
            || cap.getSawn().contains(PlayerBone.RIGHT_ARM);
        return !isAmputated && super.mayPickup(player);
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        final Player player = ((Inventory) this.container).player;
        final PlayerBones cap = player.getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
        if (cap == null)
            return super.allowModification(player);
        final boolean isAmputated = cap.getSawn().contains(PlayerBone.LEFT_ARM)
            || cap.getSawn().contains(PlayerBone.RIGHT_ARM);
        return !isAmputated && super.allowModification(player);
    }
}
