package dev.turtywurty.turtyissinking.client.models;// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.turtywurty.turtyissinking.TurtyIsSinking;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CameraModel extends Model {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
					new ResourceLocation(TurtyIsSinking.MODID, "camera"), "main");

	public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(TurtyIsSinking.MODID,
			"textures/item/camera.png");

	private final ModelPart main;
	private final ModelPart flash;
	private final ModelPart button;

	public CameraModel(ModelPart root) {
		super(RenderType::entityCutoutNoCull);
		this.main = root.getChild("main");
		this.flash = this.main.getChild("flash");
		this.button = this.main.getChild("button");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition partDefinition = meshDefinition.getRoot();

		PartDefinition main = partDefinition.addOrReplaceChild("main", CubeListBuilder.create()
				.texOffs(0, 17)
					.addBox(-4.0F, 0.0F, -2.0F, 1.0F, 6.0F, 1.0F,
							new CubeDeformation(0.0F))
				.texOffs(12, 16)
					.addBox(3.0F, 0.0F, -2.0F, 1.0F, 6.0F, 1.0F,
							new CubeDeformation(0.0F))
				.texOffs(13, 13)
					.addBox(-3.0F, 5.0F, -2.0F, 6.0F, 1.0F, 1.0F,
							new CubeDeformation(0.0F))
				.texOffs(13, 10)
					.addBox(-3.0F, 0.0F, -2.0F, 6.0F, 1.0F, 1.0F,
							new CubeDeformation(0.0F))
				.texOffs(0, 0)
					.addBox(-4.0F, 0.0F, -1.0F, 8.0F, 6.0F, 3.0F,
							new CubeDeformation(0.0F))
				.texOffs(0, 10)
					.addBox(-2.0F, 1.0F, 2.0F, 4.0F, 4.0F, 2.0F,
							new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		main.addOrReplaceChild("flash", CubeListBuilder.create()
				.texOffs(5, 17)
					.addBox(-1.0F, 6.0F, 1.0F, 2.0F, 2.0F, 1.0F,
							new CubeDeformation(0.0F))
				.texOffs(17, 16)
					.addBox(-1.0F, 8.0F, 2.0F, 2.0F, 1.0F, 1.0F,
							new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		main.addOrReplaceChild("button", CubeListBuilder.create()
				.texOffs(17, 19)
					.addBox(2.0F, 6.0F, -1.0F, 1.0F, 1.0F, 1.0F,
							new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshDefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}