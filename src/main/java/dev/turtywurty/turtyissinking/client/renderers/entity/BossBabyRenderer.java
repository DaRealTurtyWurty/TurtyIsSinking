package dev.turtywurty.turtyissinking.client.renderers.entity;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.client.models.BossBabyModel;
import dev.turtywurty.turtyissinking.entities.BossBaby;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BossBabyRenderer extends MobRenderer<BossBaby, BossBabyModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TurtyIsSinking.MODID,
        "textures/entity/boss_baby.png");
    
    public BossBabyRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new BossBabyModel(ctx.bakeLayer(BossBabyModel.LAYER_LOCATION)), 0.4f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(BossBaby pEntity) {
        return TEXTURE;
    }
}
