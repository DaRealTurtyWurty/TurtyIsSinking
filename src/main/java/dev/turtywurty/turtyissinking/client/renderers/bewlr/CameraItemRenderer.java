package dev.turtywurty.turtyissinking.client.renderers.bewlr;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.turtywurty.turtyissinking.client.Materials;
import dev.turtywurty.turtyissinking.client.models.BackpackModel;
import dev.turtywurty.turtyissinking.client.models.CameraModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CameraItemRenderer extends BlockEntityWithoutLevelRenderer {
    public static final CameraItemRenderer INSTANCE = new CameraItemRenderer(
            Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

    private final CameraModel model;

    public CameraItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
        this.model = new CameraModel(modelSet.bakeLayer(CameraModel.LAYER_LOCATION));
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemTransforms.TransformType transform, @NotNull PoseStack poseStack,
                             @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        this.model.renderToBuffer(
                poseStack,
                buffer.getBuffer(RenderType.entityCutoutNoCull(CameraModel.TEXTURE_LOCATION)),
                packedLight,
                packedOverlay,
                1.0f, 1.0f, 1.0f, 1.0f);
        poseStack.popPose();
    }
}