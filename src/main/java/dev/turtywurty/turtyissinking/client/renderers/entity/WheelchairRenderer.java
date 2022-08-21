package dev.turtywurty.turtyissinking.client.renderers.entity;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.client.models.WheelchairModel;
import dev.turtywurty.turtyissinking.entities.Wheelchair;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class WheelchairRenderer extends LivingEntityRenderer<Wheelchair, WheelchairModel<Wheelchair>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TurtyIsSinking.MODID,
        "textures/entities/wheelchair.png");
    
    public WheelchairRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new WheelchairModel<>(pContext.bakeLayer(WheelchairModel.LAYER_LOCATION)), 0.5f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(Wheelchair pEntity) {
        return TEXTURE;
    }
}
