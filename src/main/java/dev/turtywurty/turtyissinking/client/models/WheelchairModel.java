package dev.turtywurty.turtyissinking.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.entities.Wheelchair;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class WheelchairModel<T extends Wheelchair> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION =
			new ModelLayerLocation(new ResourceLocation(TurtyIsSinking.MODID, "wheelchair"), "main");
	private final ModelPart frame;
	private final ModelPart seat;
	private final ModelPart feet;

	private final ModelPart wheels;
	private final ModelPart backRight, backLeft, frontRight, frontLeft;

	private final ModelPart nitroHolder1;
	private final ModelPart nitroHolder2;
	private final ModelPart canister1;
	private final ModelPart canister2;

	public WheelchairModel(ModelPart root) {
		this.frame = root.getChild("Frame");
		this.seat = root.getChild("Seat");
		this.feet = root.getChild("Feet");

		this.wheels = root.getChild("Wheels");
		this.backRight = this.wheels.getChild("BackRight");
		this.backLeft = this.wheels.getChild("BackLeft");
		this.frontRight = this.wheels.getChild("FrontRight");
		this.frontLeft = this.wheels.getChild("FrontLeft");

		this.nitroHolder1 = root.getChild("NitroHolder");
		this.nitroHolder2 = root.getChild("NitroHolder2");
		this.canister1 = this.nitroHolder1.getChild("Canister1");
		this.canister2 = this.nitroHolder2.getChild("Canister2");
	}

	public static LayerDefinition createBodyLayer() {
		var meshDefinition = new MeshDefinition();
		PartDefinition partDefinition = meshDefinition.getRoot();

		partDefinition.addOrReplaceChild("Frame", CubeListBuilder.create().texOffs(44, 44).addBox(-7.0F, 7.6667F, -7.625F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(12, 43).addBox(6.0F, 7.6667F, -7.625F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(40, 17).addBox(6.0F, 1.6667F, -7.625F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(40, 31).addBox(-7.0F, 1.6667F, -7.625F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 43).addBox(6.0F, -4.3333F, -6.625F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(22, 43).addBox(-7.0F, -4.3333F, -6.625F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(5.0F, -5.3333F, -7.625F, 3.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(18, 15).addBox(-8.0F, -5.3333F, -7.625F, 3.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(26, 28).addBox(6.0F, -1.3333F, -7.625F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(38, 2).addBox(-7.0F, -1.3333F, -7.625F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(6.0F, -4.3333F, 3.375F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(7, 14).addBox(-7.0F, -4.3333F, 3.375F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(18, 14).addBox(6.0F, -3.3333F, -6.625F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(25, 14).addBox(-7.0F, -3.3333F, -6.625F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(36, 54).addBox(6.0F, -11.3333F, 4.375F, 1.0F, 13.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 54).addBox(-7.0F, -11.3333F, 4.375F, 1.0F, 13.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 36).addBox(-7.0F, -11.3333F, 5.375F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(37, 8).addBox(6.0F, -11.3333F, 5.375F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(48, 30).addBox(6.0F, 1.6667F, 4.375F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(36, 17).addBox(-7.0F, 1.6667F, 4.375F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 30).addBox(6.0F, 1.6667F, -8.625F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 43).addBox(-7.0F, 1.6667F, -8.625F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 28).addBox(6.0F, 6.6667F, 1.375F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(37, 0).addBox(-7.0F, 6.6667F, 1.375F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.3333F, 1.625F));

		partDefinition.addOrReplaceChild("Seat", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 2.75F, -10.0F, 12.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 28).addBox(-6.0F, -10.25F, 3.0F, 12.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.25F, 3.0F));

		PartDefinition feet = partDefinition.addOrReplaceChild("Feet", CubeListBuilder.create(), PartPose.offset(0.0F, 19.2678F, -10.3284F));

		feet.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(34, 44).addBox(-4.5F, 4.5F, -3.5F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(52, 0).addBox(-13.5F, 4.5F, -3.5F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(4, 43).addBox(-13.475F, -1.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(44, 30).addBox(-0.525F, -1.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -1.7678F, 2.8284F, -0.7854F, 0.0F, 0.0F));

		PartDefinition wheels = partDefinition.addOrReplaceChild("Wheels", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		wheels.addOrReplaceChild("BackRight", CubeListBuilder.create().texOffs(14, 54).addBox(-0.5F, 2.0F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 54).addBox(-0.5F, -3.0F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(7, 6).addBox(-0.5F, -2.0F, -3.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(7, 0).addBox(-0.5F, -2.0F, 2.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, -3.0F, 5.0F));

		wheels.addOrReplaceChild("BackLeft", CubeListBuilder.create().texOffs(52, 5).addBox(-0.5F, 2.0F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(36, 17).addBox(-0.5F, -3.0F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-0.5F, -2.0F, -3.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.5F, -2.0F, 2.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, -3.0F, 5.0F));

		wheels.addOrReplaceChild("FrontRight", CubeListBuilder.create().texOffs(44, 17).addBox(-0.5F, 0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(23, 43).addBox(-0.5F, -1.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(26, 30).addBox(-0.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 28).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, -1.5F, -6.5F));

		wheels.addOrReplaceChild("FrontLeft", CubeListBuilder.create().texOffs(12, 43).addBox(-0.5F, 0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(42, 9).addBox(-0.5F, -1.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(25, 17).addBox(-0.5F, -0.5F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(18, 17).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, -1.5F, -6.5F));

		PartDefinition nitroHolder1 = partDefinition.addOrReplaceChild("NitroHolder", CubeListBuilder.create().texOffs(18, 24).addBox(1.0F, -0.5F, 0.7F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 20).addBox(-5.0F, -0.5F, 0.7F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(18, 14).addBox(-6.0F, -0.5F, -3.3F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(22, 54).addBox(-1.0F, -0.5F, -3.3F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(5.0F, -0.5F, -3.3F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.5F, 10.3F));

		nitroHolder1.addOrReplaceChild("Canister1", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 3.6667F, -1.3F));

		PartDefinition nitroHolder2 = partDefinition.addOrReplaceChild("NitroHolder2", CubeListBuilder.create().texOffs(0, 24).addBox(1.0F, -0.5F, -2.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(18, 22).addBox(-5.0F, -0.5F, -2.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 22).addBox(-5.0F, -0.5F, 1.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(18, 20).addBox(1.0F, -0.5F, 1.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-6.0F, -0.5F, -2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(54, 17).addBox(-1.0F, -0.5F, -2.5F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(5.0F, -0.5F, -2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.5F, 9.5F));

		nitroHolder2.addOrReplaceChild("Canister2", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -0.3333F, -0.5F));

		return LayerDefinition.create(meshDefinition, 128, 128);
	}

	@Override
	public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
						  float headPitch) {
		this.backLeft.xRot = limbSwing / 5f;
		this.backRight.xRot = limbSwing / 5f;
		this.frontLeft.xRot = limbSwing / 1.25f;
		this.frontRight.xRot = limbSwing / 1.25f;
	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight,
							   int packedOverlay, float red, float green, float blue, float alpha) {
		this.frame.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.seat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.feet.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.wheels.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.nitroHolder1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.nitroHolder2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}