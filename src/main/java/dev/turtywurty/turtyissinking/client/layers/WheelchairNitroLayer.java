package dev.turtywurty.turtyissinking.client.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.turtywurty.turtyissinking.client.models.WheelchairModel;
import dev.turtywurty.turtyissinking.entities.Wheelchair;
import dev.turtywurty.turtyissinking.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WheelchairNitroLayer<T extends Wheelchair, M extends WheelchairModel<T>> extends RenderLayer<T, M> {
    public WheelchairNitroLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight,
                       @NotNull T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick,
                       float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        ItemStack slot0 = pLivingEntity.getStackInSlot(0);
        ItemStack slot1 = pLivingEntity.getStackInSlot(1);

        if (slot0.isEmpty() && slot1.isEmpty())
            return;

        if(slot0.is(ItemInit.NITRO_CANISTER.get())) {
            renderItemModel(pPoseStack, pBuffer, pPackedLight, slot0, true);
        }

        if(slot1.is(ItemInit.NITRO_CANISTER.get())) {
            renderItemModel(pPoseStack, pBuffer, pPackedLight, slot1, false);
        }
    }

    private void renderItemModel(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, ItemStack pStack, boolean left) {
        pPoseStack.pushPose();
        pPoseStack.scale(1.0f, -1.0f, -1.0f);
        pPoseStack.translate(left ? 0.19D : -0.19D, -0.5D, -0.55D);
        Minecraft.getInstance().getItemRenderer().renderStatic(
                pStack,
                ItemTransforms.TransformType.FIXED,
                pPackedLight,
                15728880,
                pPoseStack,
                pBuffer,
                0);
        pPoseStack.popPose();
    }
}
