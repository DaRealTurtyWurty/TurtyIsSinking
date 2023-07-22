package dev.turtywurty.turtyissinking.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.entities.Wheelchair;
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

public class WheelchairModel<Entity extends Wheelchair> extends EntityModel<Entity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
        new ResourceLocation(TurtyIsSinking.MODID, "wheelchair"), "main");

    private final ModelPart frame;
    private final ModelPart seat;
    private final ModelPart feet;
    private final ModelPart wheels;
    
    private final ModelPart backLeft, backRight, frontLeft, frontRight;
    
    public WheelchairModel(ModelPart root) {
        this.frame = root.getChild("frame");
        this.seat = root.getChild("seat");
        this.feet = root.getChild("feet");
        this.wheels = root.getChild("wheels");
        this.backLeft = this.wheels.getChild("BackLeft");
        this.backRight = this.wheels.getChild("BackRight");
        this.frontLeft = this.wheels.getChild("FrontLeft");
        this.frontRight = this.wheels.getChild("FrontRight");
    }
    
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
        float red, float green, float blue, float alpha) {
        this.frame.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.seat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.feet.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.wheels.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
    
    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
        float headPitch) {
        this.backLeft.xRot = limbSwing / 5f;
        this.backRight.xRot = limbSwing / 5f;
        this.frontLeft.xRot = limbSwing / 1.25f;
        this.frontRight.xRot = limbSwing / 1.25f;
    }
    
    public static LayerDefinition createBodyLayer() {
        final var meshDefinition = new MeshDefinition();
        final PartDefinition partDefinition = meshDefinition.getRoot();
        
        partDefinition.addOrReplaceChild("frame",
            CubeListBuilder.create().texOffs(44, 44)
                .addBox(-7.0F, 7.6667F, -7.625F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(12, 43)
                .addBox(6.0F, 7.6667F, -7.625F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(40, 17)
                .addBox(6.0F, 1.6667F, -7.625F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(40, 31)
                .addBox(-7.0F, 1.6667F, -7.625F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(0, 43)
                .addBox(6.0F, -4.3333F, -6.625F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(22, 43)
                .addBox(-7.0F, -4.3333F, -6.625F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(0, 14)
                .addBox(5.0F, -5.3333F, -7.625F, 3.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(18, 15)
                .addBox(-8.0F, -5.3333F, -7.625F, 3.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(26, 28)
                .addBox(6.0F, -1.3333F, -7.625F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(38, 2)
                .addBox(-7.0F, -1.3333F, -7.625F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
                .addBox(6.0F, -4.3333F, 3.375F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 0)
                .addBox(-7.0F, -4.3333F, 3.375F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 14)
                .addBox(6.0F, -3.3333F, -6.625F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 14)
                .addBox(-7.0F, -3.3333F, -6.625F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(28, 54)
                .addBox(6.0F, -11.3333F, 4.375F, 1.0F, 13.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(32, 54)
                .addBox(-7.0F, -11.3333F, 4.375F, 1.0F, 13.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 8)
                .addBox(-7.0F, -11.3333F, 5.375F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(5, 9)
                .addBox(6.0F, -11.3333F, 5.375F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(37, 0)
                .addBox(6.0F, 1.6667F, 4.375F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 17)
                .addBox(-7.0F, 1.6667F, 4.375F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 14)
                .addBox(6.0F, 1.6667F, -8.625F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(22, 14)
                .addBox(-7.0F, 1.6667F, -8.625F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
                .addBox(6.0F, 6.6667F, 1.375F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 14)
                .addBox(-7.0F, 6.6667F, 1.375F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 12.3333F, 1.625F));
        
        partDefinition.addOrReplaceChild("seat",
            CubeListBuilder.create().texOffs(0, 0)
                .addBox(-6.0F, 2.75F, -10.0F, 12.0F, 1.0F, 13.0F, new CubeDeformation(0.0F)).texOffs(0, 28)
                .addBox(-6.0F, -10.25F, 3.0F, 12.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 12.25F, 3.0F));
        
        final PartDefinition feet = partDefinition.addOrReplaceChild("feet", CubeListBuilder.create(),
            PartPose.offset(0.0F, 19.2678F, -10.3284F));
        
        feet.addOrReplaceChild("cube_r1",
            CubeListBuilder.create().texOffs(34, 44)
                .addBox(-4.5F, 4.5F, -3.5F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(52, 0)
                .addBox(-13.5F, 4.5F, -3.5F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(26, 14)
                .addBox(-13.475F, -1.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(34, 28)
                .addBox(-0.525F, -1.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(6.5F, -1.7678F, 2.8284F, -0.7854F, 0.0F, 0.0F));
        
        final PartDefinition wheels = partDefinition.addOrReplaceChild("wheels", CubeListBuilder.create(),
            PartPose.offset(0.0F, 24.0F, 0.0F));
        
        wheels.addOrReplaceChild("BackRight",
            CubeListBuilder.create().texOffs(14, 54)
                .addBox(-0.5F, 2.0F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 54)
                .addBox(-0.5F, -3.0F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(40, 30)
                .addBox(-0.5F, -2.0F, -3.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(37, 6).addBox(-0.5F,
                    -2.0F, 2.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
            PartPose.offset(-7.5F, -3.0F, 5.0F));
        
        wheels.addOrReplaceChild("BackLeft",
            CubeListBuilder.create().texOffs(52, 5)
                .addBox(-0.5F, 2.0F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(36, 17)
                .addBox(-0.5F, -3.0F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(34, 35)
                .addBox(-0.5F, -2.0F, -3.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(26, 21).addBox(-0.5F,
                    -2.0F, 2.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
            PartPose.offset(7.5F, -3.0F, 5.0F));
        
        wheels.addOrReplaceChild("FrontRight",
            CubeListBuilder.create().texOffs(26, 36)
                .addBox(-0.5F, 0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(26, 32)
                .addBox(-0.5F, -1.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(18, 24)
                .addBox(-0.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 23).addBox(-0.5F,
                    -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
            PartPose.offset(-7.5F, -1.5F, -6.5F));
        
        wheels.addOrReplaceChild("FrontLeft",
            CubeListBuilder.create().texOffs(26, 28)
                .addBox(-0.5F, 0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 22)
                .addBox(-0.5F, -1.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(5, 22)
                .addBox(-0.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 7).addBox(-0.5F,
                    -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
            PartPose.offset(7.5F, -1.5F, -6.5F));
        
        return LayerDefinition.create(meshDefinition, 128, 128);
    }
}