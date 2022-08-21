package dev.turtywurty.turtyissinking.client;

import com.mojang.blaze3d.platform.InputConstants;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public final class Keys {
    public static final Keys INSTANCE = new Keys();

    public final KeyMapping openBackpack = new KeyMapping("key." + TurtyIsSinking.MODID + ".open_backpack",
        KeyConflictContext.IN_GAME, InputConstants.getKey(InputConstants.KEY_B, -1),
        "key.categories." + TurtyIsSinking.MODID);
}
