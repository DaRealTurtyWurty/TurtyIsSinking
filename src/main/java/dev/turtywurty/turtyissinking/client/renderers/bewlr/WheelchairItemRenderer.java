package dev.turtywurty.turtyissinking.client.renderers.bewlr;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.turtywurty.turtyissinking.client.renderers.entity.WheelchairRenderer;
import dev.turtywurty.turtyissinking.entities.Wheelchair;
import dev.turtywurty.turtyissinking.init.EntityInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class WheelchairItemRenderer extends BlockEntityWithoutLevelRenderer {
    public static final WheelchairItemRenderer INSTANCE = new WheelchairItemRenderer(
        Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

    private Wheelchair wheelchair;
    private WheelchairRenderer renderer;

    public WheelchairItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull TransformType transform, @NotNull PoseStack poseStack,
                             @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if(level == null) return;

        if (this.wheelchair == null || this.renderer == null) {
            this.wheelchair = new Wheelchair(EntityInit.WHEELCHAIR.get(), level);
            this.renderer = (WheelchairRenderer) mc.getEntityRenderDispatcher().getRenderer(this.wheelchair);
        }

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.translate(-0.5D, 0.0D, -0.5D);
        this.wheelchair.animationPosition = mc.player.animationPosition;
        this.wheelchair.animationSpeed = mc.player.animationSpeed;
        this.wheelchair.animationSpeedOld = mc.player.animationSpeedOld;
        this.renderer.render(this.wheelchair, 0, mc.getPartialTick(), poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}