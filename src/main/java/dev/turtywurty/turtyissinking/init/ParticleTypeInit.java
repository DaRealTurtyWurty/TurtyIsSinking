package dev.turtywurty.turtyissinking.init;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.client.particles.PlayerSkinParticleOption;
import dev.turtywurty.turtyissinking.client.particles.PlayerSkinParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleTypeInit {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, TurtyIsSinking.MOD_ID);

    public static final RegistryObject<ParticleType<PlayerSkinParticleOption>> PLAYER_SKIN =
            PARTICLE_TYPES.register("player_skin", PlayerSkinParticleType::new);
}
