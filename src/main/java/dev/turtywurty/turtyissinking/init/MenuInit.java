package dev.turtywurty.turtyissinking.init;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.menus.BackpackMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class MenuInit {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
        TurtyIsSinking.MODID);
    
    public static final RegistryObject<MenuType<BackpackMenu>> BACKPACK = MENU_TYPES.register("backpack",
        () -> new MenuType<>(BackpackMenu::new));
}
