package dev.turtywurty.turtyissinking;

import dev.turtywurty.turtyissinking.init.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TurtyIsSinking.MODID)
public class TurtyIsSinking {
    public static final String MODID = "turtyissinking";
    public static final Logger LOGGER = LoggerFactory.getLogger(TurtyIsSinking.class);

    private static CreativeModeTab TAB;

    public static CreativeModeTab getTab() {
        return TAB;
    }

    public static void setTab(CreativeModeTab tab) {
        if (TAB == null && tab != null)
            TAB = tab;
    }
    
    public TurtyIsSinking() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemInit.ITEMS.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockEntityInit.BLOCK_ENTITIES.register(bus);
        MenuInit.MENU_TYPES.register(bus);
        EnchantmentInit.ENCHANTMENTS.register(bus);
        EntityInit.ENTITIES.register(bus);
        ParticleTypeInit.PARTICLE_TYPES.register(bus);
    }
}
