package dev.turtywurty.turtyissinking.client.particles;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen;
import dev.turtywurty.turtyissinking.init.ParticleTypeInit;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record PlayerSkinParticleOption(GameProfile playerProfile, BonesawScreen.PlayerBone bone) implements ParticleOptions {
    static final PlayerSkinParticleOption DEFAULT = new PlayerSkinParticleOption(null, null);

    public static final Deserializer<PlayerSkinParticleOption> DESERIALIZER = new Deserializer<>() {
        @Override
        public @NotNull PlayerSkinParticleOption fromCommand(@NotNull ParticleType<PlayerSkinParticleOption> pParticleType, StringReader pReader) throws CommandSyntaxException {
            UUID uuid = UUID.fromString(pReader.readQuotedString().replace("\"", ""));
            BonesawScreen.PlayerBone bone = BonesawScreen.PlayerBone.valueOf(pReader.readUnquotedString().toUpperCase());

            var profile = new GameProfile(uuid, null);
            return new PlayerSkinParticleOption(profile, bone);
        }

        @Override
        public @NotNull PlayerSkinParticleOption fromNetwork(@NotNull ParticleType<PlayerSkinParticleOption> pParticleType, FriendlyByteBuf pBuffer) {
            return new PlayerSkinParticleOption(pBuffer.readGameProfile(), pBuffer.readEnum(BonesawScreen.PlayerBone.class));
        }
    };

    @Override
    public @NotNull ParticleType<?> getType() {
        return ParticleTypeInit.PLAYER_SKIN.get();
    }

    @Override
    public void writeToNetwork(@NotNull FriendlyByteBuf pBuffer) {
        pBuffer.writeGameProfile(this.playerProfile);
        pBuffer.writeEnum(this.bone);
    }

    @Override
    public @NotNull String writeToString() {
        return this.playerProfile.getId().toString() + " " + this.bone.name();
    }
}