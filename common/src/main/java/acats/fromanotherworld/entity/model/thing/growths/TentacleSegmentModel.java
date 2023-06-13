package acats.fromanotherworld.entity.model.thing.growths;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class TentacleSegmentModel extends EntityModel<Entity> {
	private final ModelPart bone;

	public TentacleSegmentModel(ModelPart root) {
		this.bone = root.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(8, 8).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, 0.0F, -8.0F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(0.0F, -3.0F, -8.0F, 0.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	public void setRotation(float xRot, float yRot, float zRot){
		bone.setRotation(xRot, yRot, zRot);
	}

	public void setPosition(float x, float y, float z){
		bone.setPos(x * 16.0F, y * 16.0F, z * 16.0F);
	}

	public void setScale(float scale){
		bone.xScale = scale;
		bone.yScale = scale;
		bone.zScale = scale;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}