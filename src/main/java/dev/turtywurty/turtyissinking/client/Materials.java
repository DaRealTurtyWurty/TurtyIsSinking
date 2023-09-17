package dev.turtywurty.turtyissinking.client;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public final class Materials {
    public static final Material BACKPACK_LOCATION = new Material(InventoryMenu.BLOCK_ATLAS,
        new ResourceLocation(TurtyIsSinking.MODID, "block/backpack"));
}
