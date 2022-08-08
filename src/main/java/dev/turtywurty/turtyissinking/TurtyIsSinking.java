package dev.turtywurty.turtyissinking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TurtyIsSinking.MODID)
public class TurtyIsSinking {
    public static final String MODID = "turtyissinking";
    public static final Logger LOGGER = LoggerFactory.getLogger(TurtyIsSinking.class);

    public TurtyIsSinking() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    }
}
