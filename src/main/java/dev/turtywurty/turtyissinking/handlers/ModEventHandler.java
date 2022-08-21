package dev.turtywurty.turtyissinking.handlers;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBones;
import dev.turtywurty.turtyissinking.init.EntityInit;
import dev.turtywurty.turtyissinking.networking.PacketHandler;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = TurtyIsSinking.MODID, bus = Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(Hooks::common);
    }
    
    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) {
        Hooks.entityAttribs(event);
    }
    
    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        Hooks.registerCaps(event);
    }

    private static final class Hooks {
        private static void common() {
            PacketHandler.init();
        }
        
        private static void entityAttribs(EntityAttributeCreationEvent event) {
            event.put(EntityInit.BOSS_BABY.get(), Mob.createMobAttributes().build());
            event.put(EntityInit.WHEELCHAIR.get(), Mob.createMobAttributes().add(Attributes.JUMP_STRENGTH)
                .add(Attributes.MAX_HEALTH, 1.0D).add(Attributes.MOVEMENT_SPEED, 0.225F).build());
        }
        
        private static void registerCaps(RegisterCapabilitiesEvent event) {
            event.register(PlayerBones.class);
            TurtyIsSinking.LOGGER.info("Capability registered");
        }
    }
}
