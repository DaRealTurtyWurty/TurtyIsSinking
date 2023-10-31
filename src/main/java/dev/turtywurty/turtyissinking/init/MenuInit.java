package dev.turtywurty.turtyissinking.init;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.menus.BackpackMenu;
import dev.turtywurty.turtyissinking.menus.WheelchairMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class MenuInit {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
        TurtyIsSinking.MOD_ID);
    
    public static final RegistryObject<MenuType<BackpackMenu>> BACKPACK = MENU_TYPES.register("backpack",
        () -> new MenuType<>(BackpackMenu::new));

    public static final RegistryObject<MenuType<WheelchairMenu>> WHEELCHAIR = MENU_TYPES.register("wheelchair",
        () -> IForgeMenuType.create(WheelchairMenu::new));
}
