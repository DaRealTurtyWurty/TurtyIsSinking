package dev.turtywurty.turtyissinking.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.entities.BossBaby;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class BossBabyModel extends EntityModel<BossBaby> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
        new ResourceLocation(TurtyIsSinking.MOD_ID, "boss_baby"), "main");
    
    private final ModelPart body;
    
    public BossBabyModel(ModelPart root) {
        this.body = root.getChild("body");
    }
    
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
        float red, float green, float blue, float alpha) {
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
    
    @Override
    public void setupAnim(BossBaby entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch) {
        
    }
    
    public static LayerDefinition createBodyLayer() {
        final MeshDefinition meshdefinition = new MeshDefinition();
        final PartDefinition partdefinition = meshdefinition.getRoot();
        
        final PartDefinition body = partdefinition.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(12, 7)
                .addBox(-3.0F, -10.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-3.0F,
                    -8.0F, -2.0F, 6.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 24.0F, 0.0F));
        
        body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, -14.0F, -2.0F, 4.0F, 4.0F,
            4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        
        body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 15).addBox(3.0F, -9.0F, -1.0F, 2.0F, 4.0F,
            2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        
        body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(14, 14).addBox(-5.0F, -9.0F, -1.0F, 2.0F,
            4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        
        body.addOrReplaceChild("RightLeg",
            CubeListBuilder.create().texOffs(13, 20)
                .addBox(-3.0F, -4.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(5, 18).addBox(-3.0F,
                    -1.0F, -2.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 0.0F, 0.0F));
        
        body.addOrReplaceChild("LeftLeg",
            CubeListBuilder.create().texOffs(20, 11)
                .addBox(1.0F, -4.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(18, 0).addBox(1.0F,
                    -1.0F, -2.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 0.0F, 0.0F));
        
        return LayerDefinition.create(meshdefinition, 32, 32);
    }
}