package dev.turtywurty.turtyissinking.client.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSkinParticle extends SingleQuadParticle {
    private final PlayerSkinParticleOption option;
    private final DynamicTexture texture;

    public PlayerSkinParticle(ClientLevel pLevel, double pX, double pY, double pZ, PlayerSkinParticleOption option) {
        super(pLevel, pX, pY, pZ);
        this.option = option;

        this.texture = new DynamicTexture(16, 16, false);
        Minecraft.getInstance().getTextureManager().register(
                "player_skin_particle_" + this.option.bone().name().toLowerCase(),
                this.texture);
    }

    @Override
    public void render(@NotNull VertexConsumer pBuffer, @NotNull Camera pRenderInfo, float pPartialTicks) {
        RenderSystem.setShaderTexture(0, this.texture.getId());
        super.render(pBuffer, pRenderInfo, pPartialTicks);
    }

    @Override
    protected float getU0() {
        return switch (this.option.bone()) {
            case HEAD -> 0.125F;
            case BODY -> 0.25F;
            case LEFT_ARM -> 0.375F;
            case RIGHT_ARM -> 0.5F;
            case LEFT_LEG -> 0.625F;
            case RIGHT_LEG -> 0.75F;
        };
    }

    @Override
    protected float getU1() {
        return switch (this.option.bone()) {
            case HEAD -> 0.25F;
            case BODY -> 0.375F;
            case LEFT_ARM -> 0.5F;
            case RIGHT_ARM -> 0.625F;
            case LEFT_LEG -> 0.75F;
            case RIGHT_LEG -> 0.875F;
        };
    }

    @Override
    protected float getV0() {
        return switch (this.option.bone()) {
            case HEAD -> 0.0F;
            case BODY -> 0.0F;
            case LEFT_ARM -> 0.0F;
            case RIGHT_ARM -> 0.0F;
            case LEFT_LEG -> 0.0F;
            case RIGHT_LEG -> 0.0F;
        };
    }

    @Override
    protected float getV1() {
        return switch (this.option.bone()) {
            case HEAD -> 0.25F;
            case BODY -> 0.25F;
            case LEFT_ARM -> 0.25F;
            case RIGHT_ARM -> 0.25F;
            case LEFT_LEG -> 0.25F;
            case RIGHT_LEG -> 0.25F;
        };
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static class Provider implements ParticleProvider<PlayerSkinParticleOption> {
        public static final Provider INSTANCE = new Provider();

        @Nullable
        @Override
        public PlayerSkinParticle createParticle(@NotNull PlayerSkinParticleOption pType, @NotNull ClientLevel pLevel,
                                       double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new PlayerSkinParticle(pLevel, pX, pY, pZ, pType);
        }
    }
}
