package dev.turtywurty.turtyissinking.client.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.client.models.ThighHighsModel;
import dev.turtywurty.turtyissinking.items.ThighHighsItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ThighHighsLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends ThighHighsModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TurtyIsSinking.MODID, "textures/entity/thigh_highs.png");

    private final A model;

    public ThighHighsLayer(RenderLayerParent<T, M> pRenderer, A pInnerModel) {
        super(pRenderer);
        this.model = pInnerModel;
    }

    public static <T extends LivingEntity, M extends HumanoidModel<T>> void addTo(LivingEntityRenderer<T, M> renderer, EntityModelSet modelSet) {
        renderer.addLayer(new ThighHighsLayer<>(renderer, new ThighHighsModel<>(modelSet.bakeLayer(ThighHighsModel.LAYER_LOCATION))));
    }

    public void render(@NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight,
                       @NotNull T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks,
                       float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        ItemStack stack = pLivingEntity.getItemBySlot(EquipmentSlot.LEGS);
        if (stack.getItem() instanceof ThighHighsItem) {
            getParentModel().copyPropertiesTo(this.model);
            this.model.leftLeg.visible = getParentModel().leftLeg.visible;
            this.model.rightLeg.visible = getParentModel().rightLeg.visible;
            this.model.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            boolean isFoil = stack.hasFoil();
            renderModel(pPoseStack, pBuffer, pPackedLight, isFoil, this.model);
        }
    }

    private void renderModel(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, boolean pWithGlint, Model pModel) {
        VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(pBuffer, RenderType.armorCutoutNoCull(ThighHighsLayer.TEXTURE), false, pWithGlint);
        pModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}