package dev.turtywurty.turtyissinking.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.turtywurty.turtyissinking.TurtyIsSinking;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public final class KeyBindings {
    public static final KeyBindings INSTANCE = new KeyBindings();

    public final KeyMapping openBackpack = new KeyMapping("key." + TurtyIsSinking.MODID + ".open_backpack",
        KeyConflictContext.IN_GAME, InputConstants.getKey(InputConstants.KEY_B, -1),
        "key.categories." + TurtyIsSinking.MODID);

    public final KeyMapping openGallery = new KeyMapping("key." + TurtyIsSinking.MODID + ".open_gallery",
        KeyConflictContext.IN_GAME, InputConstants.getKey(InputConstants.KEY_G, -1),
        "key.categories." + TurtyIsSinking.MODID);

    public final KeyMapping wheelchairBoost = new KeyMapping("key." + TurtyIsSinking.MODID + ".wheelchair_boost",
        KeyConflictContext.IN_GAME, InputConstants.getKey(InputConstants.KEY_SPACE, -1),
        "key.categories." + TurtyIsSinking.MODID);
}
