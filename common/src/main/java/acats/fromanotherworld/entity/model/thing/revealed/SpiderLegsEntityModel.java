package acats.fromanotherworld.entity.model.thing.revealed;

import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class SpiderLegsEntityModel extends EntityModel<Entity> {
    private final ModelPart bone;
    private final ModelPart bone2;
    private final ModelPart bone3;
    private final ModelPart bone4;
    private final ModelPart bone5;
    private final ModelPart bone6;
    private final ModelPart bone7;
    private final ModelPart bone8;
    private final ModelPart bone9;
    private final ModelPart bone10;
    private final ModelPart bone11;
    private final ModelPart bone12;
    private final ModelPart bone13;
    private final ModelPart bone14;
    private final ModelPart bone15;
    private final ModelPart bone16;
    private final ModelPart bone17;
    private final ModelPart bone18;
    private final ModelPart bone19;
    private final ModelPart bone20;
    private final ModelPart bone21;
    private final ModelPart bone22;
    private final ModelPart bone23;
    private final ModelPart bone24;
    private final ModelPart bone25;
    public SpiderLegsEntityModel(ModelPart root) {
        this.bone = root.getChild("bone");
        this.bone2 = bone.getChild("Leg1").getChild("bone2");
        this.bone3 = bone2.getChild("bone3");
        this.bone4 = bone3.getChild("bone4");
        this.bone5 = bone.getChild("Leg2").getChild("bone5");
        this.bone6 = bone5.getChild("bone6");
        this.bone7 = bone6.getChild("bone7");
        this.bone8 = bone.getChild("Leg3").getChild("bone8");
        this.bone9 = bone8.getChild("bone9");
        this.bone10 = bone9.getChild("bone10");
        this.bone11 = bone.getChild("Leg4").getChild("bone11");
        this.bone12 = bone11.getChild("bone12");
        this.bone13 = bone12.getChild("bone13");
        this.bone14 = bone.getChild("Leg5").getChild("bone14");
        this.bone15 = bone14.getChild("bone15");
        this.bone16 = bone15.getChild("bone16");
        this.bone17 = bone.getChild("Leg6").getChild("bone17");
        this.bone18 = bone17.getChild("bone18");
        this.bone19 = bone18.getChild("bone19");
        this.bone20 = bone.getChild("Leg7").getChild("bone20");
        this.bone21 = bone20.getChild("bone21");
        this.bone22 = bone21.getChild("bone22");
        this.bone23 = bone.getChild("Leg8").getChild("bone23");
        this.bone24 = bone23.getChild("bone24");
        this.bone25 = bone24.getChild("bone25");
    }
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition bone = modelPartData.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

        PartDefinition Leg1 = bone.addOrReplaceChild("Leg1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.589F, 0.0F));

        PartDefinition bone2 = Leg1.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(25, 21).addBox(-11.5F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone3 = bone2.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(25, 19).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(-11.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        bone3.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(25, 17).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-11.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition Leg2 = bone.addOrReplaceChild("Leg2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.1963F, 0.0F));

        PartDefinition bone5 = Leg2.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(25, 15).addBox(-11.5F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone6 = bone5.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(25, 13).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(-11.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        bone6.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(25, 11).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-11.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition Leg3 = bone.addOrReplaceChild("Leg3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1963F, 0.0F));

        PartDefinition bone8 = Leg3.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(25, 9).addBox(-11.5F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone9 = bone8.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(25, 7).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(-11.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        bone9.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(25, 5).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-11.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition Leg4 = bone.addOrReplaceChild("Leg4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.589F, 0.0F));

        PartDefinition bone11 = Leg4.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(25, 3).addBox(-11.5F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone12 = bone11.addOrReplaceChild("bone12", CubeListBuilder.create().texOffs(25, 1).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(-11.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        bone12.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(0, 24).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-11.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition Leg5 = bone.addOrReplaceChild("Leg5", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.5525F, 0.0F));

        PartDefinition bone14 = Leg5.addOrReplaceChild("bone14", CubeListBuilder.create().texOffs(0, 22).addBox(-11.5F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone15 = bone14.addOrReplaceChild("bone15", CubeListBuilder.create().texOffs(0, 20).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(-11.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        bone15.addOrReplaceChild("bone16", CubeListBuilder.create().texOffs(0, 18).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-11.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition Leg6 = bone.addOrReplaceChild("Leg6", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.9452F, 0.0F));

        PartDefinition bone17 = Leg6.addOrReplaceChild("bone17", CubeListBuilder.create().texOffs(0, 16).addBox(-11.5F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone18 = bone17.addOrReplaceChild("bone18", CubeListBuilder.create().texOffs(0, 14).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(-11.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        bone18.addOrReplaceChild("bone19", CubeListBuilder.create().texOffs(0, 12).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-11.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition Leg7 = bone.addOrReplaceChild("Leg7", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.9452F, 0.0F));

        PartDefinition bone20 = Leg7.addOrReplaceChild("bone20", CubeListBuilder.create().texOffs(0, 10).addBox(-11.5F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone21 = bone20.addOrReplaceChild("bone21", CubeListBuilder.create().texOffs(0, 8).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(-11.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        bone21.addOrReplaceChild("bone22", CubeListBuilder.create().texOffs(0, 6).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-11.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        PartDefinition Leg8 = bone.addOrReplaceChild("Leg8", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.5525F, 0.0F));

        PartDefinition bone23 = Leg8.addOrReplaceChild("bone23", CubeListBuilder.create().texOffs(0, 4).addBox(-11.5F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition bone24 = bone23.addOrReplaceChild("bone24", CubeListBuilder.create().texOffs(0, 2).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(-11.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        bone24.addOrReplaceChild("bone25", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-11.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));
        return LayerDefinition.create(modelData, 64, 64);
    }
    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        PossibleDisguisedThing thing = (PossibleDisguisedThing) entity;
        float p = Mth.clamp(10.0F * (1.0F - Math.abs((float)thing.getRevealMaximum() - (float)thing.getTimeUntilFinishedRevealing()) / (float)thing.getRevealMaximum()), 0, 1);

        float rotation = Mth.lerp(p, -(float)Math.PI / 2, 0.7854F);
        float rotation2 = Mth.lerp(p, -(float)Math.PI, -1.1781F);

        bone2.zRot = rotation;
        bone3.zRot = rotation2;
        bone4.zRot = rotation2;
        bone5.zRot = rotation;
        bone6.zRot = rotation2;
        bone7.zRot = rotation2;
        bone8.zRot = rotation;
        bone9.zRot = rotation2;
        bone10.zRot = rotation2;
        bone11.zRot = rotation;
        bone12.zRot = rotation2;
        bone13.zRot = rotation2;
        bone14.zRot = rotation;
        bone15.zRot = rotation2;
        bone16.zRot = rotation2;
        bone17.zRot = rotation;
        bone18.zRot = rotation2;
        bone19.zRot = rotation2;
        bone20.zRot = rotation;
        bone21.zRot = rotation2;
        bone22.zRot = rotation2;
        bone23.zRot = rotation;
        bone24.zRot = rotation2;
        bone25.zRot = rotation2;
    }
    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        bone.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}