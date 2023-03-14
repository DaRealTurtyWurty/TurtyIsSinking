package dev.turtywurty.turtyissinking.client.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.turtywurty.turtyissinking.client.Materials;
import dev.turtywurty.turtyissinking.client.models.BackpackModel;
import dev.turtywurty.turtyissinking.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BackpackLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final BackpackModel backpack;

    public BackpackLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
        this.backpack = new BackpackModel(
            Minecraft.getInstance().getEntityModels().bakeLayer(BackpackModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player,
        float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
        float headPitch) {
        if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() == ItemInit.BACKPACK.get()) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180f));
            copyPlayerTransform();

            this.backpack.renderToBuffer(poseStack, Materials.BACKPACK_LOCATION.buffer(buffer, RenderType::entitySolid),
                packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    private void copyPlayerTransform() {
        final ModelPart body = getParentModel().body;
        this.backpack.backpack.xScale = body.xScale;
        this.backpack.backpack.yScale = body.yScale;
        this.backpack.backpack.zScale = body.zScale;
        this.backpack.backpack.xRot = -body.xRot;
        this.backpack.backpack.yRot = -body.yRot;
        this.backpack.backpack.zRot = -body.zRot;
        this.backpack.backpack.x = body.x;
        this.backpack.backpack.y = body.y - 1.5f;
        this.backpack.backpack.z = body.z - 2f;
    }
}
