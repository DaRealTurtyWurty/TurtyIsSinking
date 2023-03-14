package dev.turtywurty.turtyissinking.client.renderers.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.turtywurty.turtyissinking.blockentities.BackpackBlockEntity;
import dev.turtywurty.turtyissinking.blocks.BackpackBlock;
import dev.turtywurty.turtyissinking.client.Materials;
import dev.turtywurty.turtyissinking.client.models.BackpackModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class BackpackBERenderer implements BlockEntityRenderer<BackpackBlockEntity> {
    private final BackpackModel backpack;
    
    public BackpackBERenderer(BlockEntityRendererProvider.Context context) {
        this.backpack = new BackpackModel(context.bakeLayer(BackpackModel.LAYER_LOCATION));
    }
    
    @Override
    public void render(BackpackBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
        MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.mulPose(Axis.XP.rotationDegrees(180f));
        poseStack.mulPose(Axis.YP.rotationDegrees(180f));
        poseStack.translate(-0.5f, -1.25f, 0.625f);

        final Direction facing = blockEntity.getBlockState().getValue(BackpackBlock.FACING);
        poseStack.mulPose(Axis.YP.rotationDegrees(facing.toYRot() - 180));

        poseStack.translate(getX(facing), 0f, getZ(facing));

        this.backpack.renderToBuffer(poseStack, Materials.BACKPACK_LOCATION.buffer(buffer, RenderType::entitySolid),
            packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * If direction is multiple of 180:<br>
     * 1. Return 0.<br>
     * <br>
     * If direction is not multiple of 180(90):<br>
     * 1. Minus 180 (to get -90 or 90).<br>
     * 2. Divide by 90 (to get -1 or 1).<br>
     * 3. Multiply by 0.125 (to get -0.125 or 0.125).
     */
    private static float getX(Direction facing) {
        return facing.toYRot() % 180 == 0 ? 0f : (facing.toYRot() - 180) / 90f * 0.125f;
    }

    /**
     * If direction is a multiple of 180:<br>
     * 1. Minus 180 (to get -180 or 0).<br>
     * 2. Negate (to get 0 or 180).<br>
     * 3. Multiply by 2 (to get 0 or 360).<br>
     * 4. Divide by 180 (to get 0 or 2).<br>
     * 5. Multiply by 0.125 (to get 0 or 0.25).<br>
     * <br>
     * If direction is not a multiple of 180(90):<br>
     * 1. Divide 180 / 180 (to get 1).<br>
     * 2. Multiply by 0.125 (to get 0.125).
     */
    private static float getZ(Direction facing) {
        return (facing.toYRot() % 180 == 0 ? -(facing.toYRot() - 180) * 2 : 180) / 180 * 0.125f;
    }
}
