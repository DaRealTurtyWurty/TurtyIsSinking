package dev.turtywurty.turtyissinking.client.renderers.blockentity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.turtywurty.turtyissinking.blockentities.PlayerBoneBlockEntity;
import dev.turtywurty.turtyissinking.blocks.PlayerBoneBlock;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class PlayerBoneBERenderer implements BlockEntityRenderer<PlayerBoneBlockEntity> {
    private Pair<GameProfile, RenderCache> lastRendered;

    public PlayerBoneBERenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    public void render(PlayerBoneBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack,
                       @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        final PlayerBoneBlock block = (PlayerBoneBlock) pBlockEntity.getBlockState().getBlock();
        final PlayerBone bone = block.getBone();
        if (bone == PlayerBone.HEAD)
            return;

        GameProfile profile = pBlockEntity.getGameProfile();
        if (profile == null) {
            profile = new GameProfile(UUID.randomUUID(), "Steve");
        }

        RenderCache cache = loadCache(bone, profile);
        if (cache == null)
            return;

        for (final ModelPart modelPart : cache.parts().get(bone)) {
            pPoseStack.pushPose();
            modelPart.translateAndRotate(pPoseStack);
            pPoseStack.scale(1.0f, -1.0f, -1.0f);
            pPoseStack.translate(0.5, -0.75, -0.5);
            switch (bone) {
                case LEFT_ARM:
                    if (cache.modelType().equals("slim"))
                        pPoseStack.translate(-0.03, 0.0, 0.0);
                    pPoseStack.translate(0.0, 0.1249, 0.0);
                    break;
                case RIGHT_ARM:
                    if(cache.modelType().equals("slim"))
                        pPoseStack.translate(0.03, 0.0, 0.0);
                    pPoseStack.translate(0.0, 0.1249, 0.0);
                    break;
            }

            modelPart.render(
                    pPoseStack,
                    pBufferSource.getBuffer(cache.renderType()),
                    pPackedLight,
                    OverlayTexture.NO_OVERLAY);
            pPoseStack.popPose();
        }
    }

    private RenderCache loadCache(PlayerBone bone, GameProfile profile) {
        if(profile == null || this.lastRendered == null || !profile.equals(this.lastRendered.getLeft())) {
            if(profile == null)
                profile = new GameProfile(UUID.randomUUID(), "Steve");

            Map<Type, MinecraftProfileTexture> map = getSkinInformation(profile);
            final ResourceLocation texture = getSkinTexture(map, profile);
            final RenderType renderType = getRenderType(texture);
            String modelType = getModelType(profile, map);
            final PlayerRenderer renderer = getPlayerRenderer(modelType);
            if (renderer == null)
                return null;

            final PlayerModel<AbstractClientPlayer> model = renderer.getModel();
            final ModelPart[] parts = bone.getParts(model);
            List<ModelPart> partList = Stream.of(parts)
                    .map(part -> new ModelPart(part.cubes, part.children))
                    .toList();

            this.lastRendered = Pair.of(profile, new RenderCache(map, texture, renderType, modelType, model, new HashMap<>(Map.of(bone, partList))));
            return this.lastRendered.getValue();
        }

        RenderCache cache = this.lastRendered.getValue();
        if(!cache.parts().containsKey(bone)) {
            final PlayerModel<AbstractClientPlayer> model = getPlayerRenderer(cache.modelType()).getModel();
            final ModelPart[] parts = bone.getParts(model);
            List<ModelPart> partList = Stream.of(parts)
                    .map(part -> new ModelPart(part.cubes, part.children))
                    .toList();

            cache.parts().put(bone, partList);
        }

        return cache;
    }

    public static Map<Type, MinecraftProfileTexture> getSkinInformation(GameProfile profile) {
        return Minecraft.getInstance()
                .getSkinManager()
                .getInsecureSkinInformation(profile);
    }

    public static ResourceLocation getSkinTexture(Map<Type, MinecraftProfileTexture> map, GameProfile profile) {
        if (map.containsKey(Type.SKIN)) {
            return Minecraft.getInstance()
                    .getSkinManager()
                    .registerTexture(map.get(Type.SKIN), Type.SKIN);
        } else {
            return DefaultPlayerSkin.getDefaultSkin(UUIDUtil.getOrCreatePlayerUUID(profile));
        }
    }

    public static RenderType getRenderType(ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(texture);
    }

    public static String getModelType(GameProfile profile, Map<Type, MinecraftProfileTexture> map) {
        return profile == null ||
                map == null ||
                !map.containsKey(Type.SKIN) ?
                    (FMLEnvironment.production ? "default" : "slim") :
                    map.get(Type.SKIN).getMetadata("model");
    }

    public static PlayerRenderer getPlayerRenderer(String modelType) {
        return (PlayerRenderer)
                Minecraft.getInstance()
                        .getEntityRenderDispatcher()
                        .getSkinMap()
                        .get(modelType);
    }

    public static TextureAtlasSprite getSprite(ResourceLocation texture) {
        return Minecraft.getInstance()
                .getModelManager()
                .getAtlas(InventoryMenu.BLOCK_ATLAS)
                .getSprite(texture);
    }

    public static TextureAtlasSprite getSprite(GameProfile profile) {
        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures = getSkinInformation(profile);
        ResourceLocation texture = getSkinTexture(textures, profile);
        return getSprite(texture);
    }

    public record RenderCache(
            Map<Type, MinecraftProfileTexture> map,
            ResourceLocation texture,
            RenderType renderType,
            String modelType,
            PlayerModel<AbstractClientPlayer> model,
            Map<PlayerBone, List<ModelPart>> parts) {}
}
