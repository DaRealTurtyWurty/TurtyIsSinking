package dev.turtywurty.turtyissinking.client.renderers.bewlr;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.turtywurty.turtyissinking.client.Materials;
import dev.turtywurty.turtyissinking.client.models.BackpackModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BackpackItemRenderer extends BlockEntityWithoutLevelRenderer {
    public static final BackpackItemRenderer INSTANCE = new BackpackItemRenderer(
        Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    
    private final BackpackModel model;

    public BackpackItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
        this.model = new BackpackModel(modelSet.bakeLayer(BackpackModel.LAYER_LOCATION));
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull TransformType transform, @NotNull PoseStack poseStack,
                             @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        this.model.renderToBuffer(
                poseStack,
                Materials.BACKPACK_LOCATION.buffer(buffer, RenderType::entitySolid),
                packedLight,
                packedOverlay,
                1.0f, 1.0f, 1.0f, 1.0f);
    }
}