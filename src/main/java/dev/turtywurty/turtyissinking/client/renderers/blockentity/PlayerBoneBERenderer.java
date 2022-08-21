package dev.turtywurty.turtyissinking.client.renderers.blockentity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.turtywurty.turtyissinking.blockentities.PlayerBoneBlockEntity;
import dev.turtywurty.turtyissinking.blocks.PlayerBoneBlock;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.SkullBlock;

public class PlayerBoneBERenderer implements BlockEntityRenderer<PlayerBoneBlockEntity> {
    public PlayerBoneBERenderer(BlockEntityRendererProvider.Context context) {
    }
    
    @Override
    public void render(PlayerBoneBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
        MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        final var block = (PlayerBoneBlock) pBlockEntity.getBlockState().getBlock();
        final PlayerBone bone = block.getBone();
        if (bone == PlayerBone.HEAD)
            return;

        final Player player = pBlockEntity.getPlayer();
        final GameProfile playerProfile = player == null ? null : player.getGameProfile();
        final String modelType = player == null ? "default"
            : Minecraft.getInstance().getSkinManager().getInsecureSkinInformation(playerProfile).get(Type.SKIN)
                .getMetadata("model");
        final PlayerRenderer renderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher()
            .getSkinMap().get(modelType);

        final RenderType renderType = SkullBlockRenderer.getRenderType(SkullBlock.Types.PLAYER, playerProfile);

        final PlayerModel model = renderer.getModel();
        final ModelPart[] parts = bone.getParts(model);
        for (final ModelPart modelPart : parts) {
            pPoseStack.pushPose();
            modelPart.translateAndRotate(pPoseStack);
            pPoseStack.scale(1.0f, -1.0f, 1.0f);
            pPoseStack.translate(0f, -2.5f, 0f);
            modelPart.render(pPoseStack, pBufferSource.getBuffer(renderType), pPackedLight, OverlayTexture.NO_OVERLAY);
            pPoseStack.popPose();
        }
    }
}
