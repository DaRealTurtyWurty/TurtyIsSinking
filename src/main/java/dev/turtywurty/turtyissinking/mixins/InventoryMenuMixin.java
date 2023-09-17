package dev.turtywurty.turtyissinking.mixins;

import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
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
    public boolean mayPickup(@NotNull Player pPlayer) {
        final Player player = ((Inventory) this.container).player;
        final LazyOptional<PlayerBones> cap = player.getCapability(PlayerBonesCapability.PLAYER_BONES);
        if(!cap.isPresent())
            return super.mayPickup(pPlayer);

        PlayerBones bones = cap.orElse(null);
        final boolean isAmputated = bones.getSawn().contains(PlayerBone.LEFT_ARM)
            || bones.getSawn().contains(PlayerBone.RIGHT_ARM);
        return !isAmputated && super.mayPickup(pPlayer);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack pStack) {
        final Player player = ((Inventory) this.container).player;
        final LazyOptional<PlayerBones> cap = player.getCapability(PlayerBonesCapability.PLAYER_BONES);
        if(!cap.isPresent())
            return super.mayPlace(pStack);

        PlayerBones bones = cap.orElse(null);
        final boolean isAmputated = bones.getSawn().contains(PlayerBone.LEFT_ARM)
            || bones.getSawn().contains(PlayerBone.RIGHT_ARM);
        return !isAmputated && super.mayPlace(pStack);
    }
}
