package dev.turtywurty.turtyissinking.client.renderers.entity;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.client.layers.WheelchairNitroLayer;
import dev.turtywurty.turtyissinking.client.models.WheelchairModel;
import dev.turtywurty.turtyissinking.entities.Wheelchair;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class WheelchairRenderer extends LivingEntityRenderer<Wheelchair, WheelchairModel<Wheelchair>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(TurtyIsSinking.MOD_ID,
        "textures/entity/wheelchair.png");
    
    public WheelchairRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new WheelchairModel<>(pContext.bakeLayer(WheelchairModel.LAYER_LOCATION)), 0.5f);
        addLayer(new WheelchairNitroLayer<>(this));
    }
    
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Wheelchair pEntity) {
        return TEXTURE;
    }
}
