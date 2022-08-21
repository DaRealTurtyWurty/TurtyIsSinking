package dev.turtywurty.turtyissinking.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class BackpackModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
        new ResourceLocation(TurtyIsSinking.MODID, "backpack"), "main");
    public final ModelPart backpack;

    public BackpackModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.backpack = root.getChild("backpack");
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
        float red, float green, float blue, float alpha) {
        this.backpack.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public static LayerDefinition createMainLayer() {
        final MeshDefinition meshdefinition = new MeshDefinition();
        final PartDefinition partdefinition = meshdefinition.getRoot();

        final PartDefinition backpack = partdefinition.addOrReplaceChild("backpack", CubeListBuilder.create(),
            PartPose.offset(0.0F, 8.0F, 0.5F));

        backpack.addOrReplaceChild("Back",
            CubeListBuilder.create().texOffs(0, 18)
                .addBox(-2.0F, -14.0F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 8).addBox(-3.0F,
                    -13.0F, -0.5F, 6.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 16.0F, -0.5F));

        final PartDefinition Straps = backpack.addOrReplaceChild("Straps", CubeListBuilder.create(),
            PartPose.offsetAndRotation(0.0F, 8.05F, -2.05F, 0.7854F, 0.0F, 0.0F));

        final PartDefinition FontStrap = Straps.addOrReplaceChild("FontStrap", CubeListBuilder.create().texOffs(15, 20)
            .addBox(-0.5F, -0.3499F, 0.0998F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -1.4106F, -3.2286F));

        FontStrap
            .addOrReplaceChild("strap_r1",
                CubeListBuilder.create().texOffs(2, 20).addBox(1.0F, -3.5F, -8.0F, 1.0F, 0.0F, 2.0F,
                    new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.5F, -2.1249F, 6.8248F, 0.7854F, 0.0F, 0.0F));

        final PartDefinition SideStraps = Straps.addOrReplaceChild("SideStraps", CubeListBuilder.create(),
            PartPose.offset(0.0F, 10.1645F, 5.3962F));

        final PartDefinition LeftStrap = SideStraps.addOrReplaceChild("LeftStrap", CubeListBuilder.create(),
            PartPose.offset(4.3232F, -9.973F, -7.0119F));

        LeftStrap
            .addOrReplaceChild("strap_r2",
                CubeListBuilder.create().texOffs(13, 20).addBox(-0.5F, -0.3553F, 0.1053F, 1.0F, 0.0F, 1.0F,
                    new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, -0.7854F, 1.5708F));

        LeftStrap.addOrReplaceChild("strap_r3", CubeListBuilder.create().texOffs(20, 0).addBox(-0.5F, -0.18F, -1.6793F,
            1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 1.5708F));

        final PartDefinition RightStrap = SideStraps.addOrReplaceChild("RightStrap", CubeListBuilder.create(),
            PartPose.offset(-4.3268F, -9.973F, -7.0119F));

        RightStrap.addOrReplaceChild("strap_r4",
            CubeListBuilder.create().texOffs(5, 20).addBox(-0.5F, -0.2493F, 0.2114F, 1.0F, 0.0F, 1.0F,
                new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(-0.15F, 0.0F, 0.0F, -0.7854F, 0.7854F, -1.5708F));

        RightStrap
            .addOrReplaceChild("strap_r5",
                CubeListBuilder.create().texOffs(0, 2).addBox(-0.5F, -0.0268F, -1.6793F, 1.0F, 0.0F, 2.0F,
                    new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.15F, 0.0F, 0.0F, 0.0F, 0.7854F, -1.5708F));

        final PartDefinition TopStraps = Straps.addOrReplaceChild("TopStraps", CubeListBuilder.create(),
            PartPose.offset(0.0F, 10.1645F, 5.3962F));

        final PartDefinition TopLeft = TopStraps.addOrReplaceChild("TopLeft", CubeListBuilder.create(),
            PartPose.offset(1.5F, -13.3607F, -1.937F));

        TopLeft
            .addOrReplaceChild("strap_r6",
                CubeListBuilder.create().texOffs(17, 20).addBox(-0.5F, -1.025F, 1.0F, 1.0F, 0.0F, 1.0F,
                    new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.3393F, 0.137F, -0.7854F, 0.0F, 0.0F));

        TopLeft.addOrReplaceChild("strap_r7", CubeListBuilder.create().texOffs(17, 0).addBox(-0.5F, 0.0F, -1.5F, 1.0F,
            0.0F, 3.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(0.0F, -0.3643F, 0.062F, 0.0F, 0.0F, 0.0F));

        TopLeft
            .addOrReplaceChild("strap_r8",
                CubeListBuilder.create().texOffs(20, 2).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 0.0F, 2.0F,
                    new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.3393F, 0.137F, 0.7854F, 0.0F, 0.0F));

        final PartDefinition TopRight = TopStraps.addOrReplaceChild("TopRight", CubeListBuilder.create(),
            PartPose.offset(-1.5F, -13.3607F, -1.937F));

        TopRight
            .addOrReplaceChild("strap_r9",
                CubeListBuilder.create().texOffs(19, 3).addBox(-0.5F, -1.025F, 1.0F, 1.0F, 0.0F, 1.0F,
                    new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.3393F, 0.137F, -0.7854F, 0.0F, 0.0F));

        TopRight.addOrReplaceChild("strap_r10", CubeListBuilder.create().texOffs(11, 8).addBox(-0.5F, 0.0F, -1.5F, 1.0F,
            0.0F, 3.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(0.0F, -0.3643F, 0.062F, 0.0F, 0.0F, 0.0F));

        TopRight
            .addOrReplaceChild("strap_r11",
                CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 0.0F, 2.0F,
                    new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -0.3393F, 0.137F, 0.7854F, 0.0F, 0.0F));

        backpack.addOrReplaceChild("Compartments",
            CubeListBuilder.create().texOffs(0, 0)
                .addBox(-4.0F, -8.0F, -4.5F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(14, 8)
                .addBox(-3.0F, -10.0F, -3.5F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(12, 16).addBox(-2.0F,
                    -12.0F, -2.5F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 16.0F, -0.5F));

        backpack.addOrReplaceChild("Pouches",
            CubeListBuilder.create().texOffs(14, 13)
                .addBox(-3.0F, -7.0F, -5.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 20)
                .addBox(-4.5F, -7.0F, -3.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(8, 18).addBox(3.5F,
                    -7.0F, -3.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 16.0F, -0.5F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
}