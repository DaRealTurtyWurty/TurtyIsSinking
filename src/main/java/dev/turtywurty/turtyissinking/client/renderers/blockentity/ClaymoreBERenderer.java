package dev.turtywurty.turtyissinking.client.renderers.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.turtywurty.turtyissinking.blockentities.ClaymoreBlockEntity;
import dev.turtywurty.turtyissinking.blocks.ClaymoreBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ClaymoreBERenderer implements BlockEntityRenderer<ClaymoreBlockEntity> {
    public ClaymoreBERenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(@NotNull ClaymoreBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack,
                       @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if(!pBlockEntity.getBlockState().getValue(ClaymoreBlock.ACTIVATED))
            return;

        VertexConsumer consumer = pBufferSource.getBuffer(RenderType.lines());
        Direction direction = pBlockEntity.getBlockState().getValue(ClaymoreBlock.FACING);

        Matrix4f matrix = pPoseStack.last().pose();
        Matrix3f normal = pPoseStack.last().normal();
        switch (direction) {
            case NORTH -> {
                consumer.vertex(matrix, 0.5F, 0.48F, 0.49F).color(0x44FF0000).normal(normal, 0, 0, -1).endVertex();
                consumer.vertex(matrix, 0.1F, 0.48F, 0F).color(0x44BB0000).normal(normal, 0, 0, 1).endVertex();

                consumer.vertex(matrix, 0.5F, 0.48F, 0.49F).color(0x44FF0000).normal(normal, 0, 0, -1).endVertex();
                consumer.vertex(matrix, 0.9F, 0.48F, 0F).color(0x44BB0000).normal(normal, 0, 0, 1).endVertex();
            }

            case SOUTH -> {
                consumer.vertex(matrix, 0.5F, 0.48F, 0.51F).color(0x44BB0000).normal(normal, 0, 0, -1).endVertex();
                consumer.vertex(matrix, 0.1F, 0.48F, 1F).color(0x44FF0000).normal(normal, 0, 0, 1).endVertex();

                consumer.vertex(matrix, 0.5F, 0.48F, 0.51F).color(0x44BB0000).normal(normal, 0, 0, -1).endVertex();
                consumer.vertex(matrix, 0.9F, 0.48F, 1F).color(0x44FF0000).normal(normal, 0, 0, 1).endVertex();
            }

            case EAST -> {
                consumer.vertex(matrix, 0.51F, 0.48F, 0.5F).color(0x44BB0000).normal(normal, -1, 0, 0).endVertex();
                consumer.vertex(matrix, 1F, 0.48F, 0.1F).color(0x44FF0000).normal(normal, 1, 0, 0).endVertex();

                consumer.vertex(matrix, 0.51F, 0.48F, 0.5F).color(0x44BB0000).normal(normal, -1, 0, 0).endVertex();
                consumer.vertex(matrix, 1F, 0.48F, 0.9F).color(0x44FF0000).normal(normal, 1, 0, 0).endVertex();
            }

            case WEST -> {
                consumer.vertex(matrix, 0.49F, 0.48F, 0.5F).color(0x44BB0000).normal(normal, -1, 0, 0).endVertex();
                consumer.vertex(matrix, 0F, 0.48F, 0.1F).color(0x44FF0000).normal(normal, 1, 0, 0).endVertex();

                consumer.vertex(matrix, 0.49F, 0.48F, 0.5F).color(0x44BB0000).normal(normal, -1, 0, 0).endVertex();
                consumer.vertex(matrix, 0F, 0.48F, 0.9F).color(0x44FF0000).normal(normal, 1, 0, 0).endVertex();
            }
        }
    }
}
