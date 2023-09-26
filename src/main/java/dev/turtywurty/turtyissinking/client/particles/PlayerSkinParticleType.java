package dev.turtywurty.turtyissinking.client.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import org.jetbrains.annotations.NotNull;

public class PlayerSkinParticleType extends ParticleType<PlayerSkinParticleOption> {
    private static final Codec<PlayerSkinParticleOption> CODEC = Codec.unit(PlayerSkinParticleOption.DEFAULT);

    public PlayerSkinParticleType() {
        super(false, PlayerSkinParticleOption.DESERIALIZER);
    }

    @Override
    public @NotNull Codec<PlayerSkinParticleOption> codec() {
        return CODEC;
    }
}