package dev.turtywurty.turtyissinking.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.turtywurty.turtyissinking.TurtyIsSinking;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class ThighHighsModel<T extends LivingEntity> extends HumanoidModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION =
			new ModelLayerLocation(new ResourceLocation(TurtyIsSinking.MOD_ID, "thigh_highs"), "main");

	private final ModelPart leftPants;
	private final ModelPart rightPants;

	public ThighHighsModel(ModelPart root) {
		super(root);
		this.leftPants = root.getChild("left_pants");
		this.rightPants = root.getChild("right_pants");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition partDefinition = meshDefinition.getRoot();

		partDefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(32, 0)
				.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F,
						new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		partDefinition.addOrReplaceChild("hat", CubeListBuilder.create()
				.texOffs(32, 0)
				.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F,
						new CubeDeformation(0.5F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		partDefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(40, 0)
				.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		partDefinition.addOrReplaceChild("jacket", CubeListBuilder.create()
				.texOffs(0, 32)
				.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F,
						new CubeDeformation(0.25F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		partDefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.texOffs(32, 48)
				.addBox(9.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offset(5.0F, 2.0F, 0.0F));

		partDefinition.addOrReplaceChild("left_sleve", CubeListBuilder.create()
				.texOffs(48, 48)
				.addBox(9.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F,
						new CubeDeformation(0.25F)),
				PartPose.offset(5.0F, 2.0F, 0.0F));

		partDefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(40, 16)
				.addBox(-13.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offset(-5.0F, 2.0F, 0.0F));

		partDefinition.addOrReplaceChild("right_sleve", CubeListBuilder.create()
				.texOffs(40, 32).
				addBox(-13.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F,
						new CubeDeformation(0.25F)),
				PartPose.offset(-5.0F, 2.0F, 0.0F));

		partDefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 12.0F, 0.0F));

		partDefinition.addOrReplaceChild("left_pants", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F,
						new CubeDeformation(0.25F)),
				PartPose.offset(1.9F, 12.0F, 0.0F));

		partDefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-6.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 12.0F, 0.0F));

		partDefinition.addOrReplaceChild("right_pants", CubeListBuilder.create()
				.texOffs(16, 16)
				.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F,
						new CubeDeformation(0.25F)),
				PartPose.offset(-1.9F, 12.0F, 0.0F));

		return LayerDefinition.create(meshDefinition, 64, 64);
	}

	@Override
	public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.leftPants.copyFrom(this.leftLeg);
		this.rightPants.copyFrom(this.rightLeg);
		this.leftPants.visible = this.leftLeg.visible;
		this.rightPants.visible = this.rightLeg.visible;
	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);

		this.leftPants.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		this.rightPants.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}