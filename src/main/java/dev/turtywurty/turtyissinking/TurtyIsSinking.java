package dev.turtywurty.turtyissinking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.turtywurty.turtyissinking.init.BlockEntityInit;
import dev.turtywurty.turtyissinking.init.BlockInit;
import dev.turtywurty.turtyissinking.init.EnchantmentInit;
import dev.turtywurty.turtyissinking.init.EntityInit;
import dev.turtywurty.turtyissinking.init.ItemInit;
import dev.turtywurty.turtyissinking.init.MenuInit;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TurtyIsSinking.MODID)
public class TurtyIsSinking {
    public static final String MODID = "turtyissinking";
    public static final Logger LOGGER = LoggerFactory.getLogger(TurtyIsSinking.class);

    public static final CreativeModeTab TAB = new CreativeModeTab(TurtyIsSinking.MODID) {
        @Override
        public ItemStack makeIcon() {
            return ItemInit.BACKPACK.get().getDefaultInstance();
        }
    };
    
    public TurtyIsSinking() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemInit.ITEMS.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockEntityInit.BLOCK_ENTITIES.register(bus);
        MenuInit.MENU_TYPES.register(bus);
        EnchantmentInit.ENCHANTMENTS.register(bus);
        EntityInit.ENTITIES.register(bus);
    }
}
