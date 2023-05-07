package acats.fromanotherworld.entity.model.revealed;

import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

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
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 17.0F, 0.0F));

        ModelPartData Leg1 = bone.addChild("Leg1", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.589F, 0.0F));

        ModelPartData bone2 = Leg1.addChild("bone2", ModelPartBuilder.create().uv(25, 21).cuboid(-11.5F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData bone3 = bone2.addChild("bone3", ModelPartBuilder.create().uv(25, 19).cuboid(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(-0.05F)), ModelTransform.of(-11.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        bone3.addChild("bone4", ModelPartBuilder.create().uv(25, 17).cuboid(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(-11.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        ModelPartData Leg2 = bone.addChild("Leg2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.1963F, 0.0F));

        ModelPartData bone5 = Leg2.addChild("bone5", ModelPartBuilder.create().uv(25, 15).cuboid(-11.5F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData bone6 = bone5.addChild("bone6", ModelPartBuilder.create().uv(25, 13).cuboid(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(-0.05F)), ModelTransform.of(-11.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        bone6.addChild("bone7", ModelPartBuilder.create().uv(25, 11).cuboid(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(-11.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        ModelPartData Leg3 = bone.addChild("Leg3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.1963F, 0.0F));

        ModelPartData bone8 = Leg3.addChild("bone8", ModelPartBuilder.create().uv(25, 9).cuboid(-11.5F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData bone9 = bone8.addChild("bone9", ModelPartBuilder.create().uv(25, 7).cuboid(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(-0.05F)), ModelTransform.of(-11.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        bone9.addChild("bone10", ModelPartBuilder.create().uv(25, 5).cuboid(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(-11.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        ModelPartData Leg4 = bone.addChild("Leg4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.589F, 0.0F));

        ModelPartData bone11 = Leg4.addChild("bone11", ModelPartBuilder.create().uv(25, 3).cuboid(-11.5F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData bone12 = bone11.addChild("bone12", ModelPartBuilder.create().uv(25, 1).cuboid(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(-0.05F)), ModelTransform.of(-11.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        bone12.addChild("bone13", ModelPartBuilder.create().uv(0, 24).cuboid(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(-11.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        ModelPartData Leg5 = bone.addChild("Leg5", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.5525F, 0.0F));

        ModelPartData bone14 = Leg5.addChild("bone14", ModelPartBuilder.create().uv(0, 22).cuboid(-11.5F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData bone15 = bone14.addChild("bone15", ModelPartBuilder.create().uv(0, 20).cuboid(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(-0.05F)), ModelTransform.of(-11.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        bone15.addChild("bone16", ModelPartBuilder.create().uv(0, 18).cuboid(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(-11.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        ModelPartData Leg6 = bone.addChild("Leg6", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.9452F, 0.0F));

        ModelPartData bone17 = Leg6.addChild("bone17", ModelPartBuilder.create().uv(0, 16).cuboid(-11.5F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData bone18 = bone17.addChild("bone18", ModelPartBuilder.create().uv(0, 14).cuboid(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(-0.05F)), ModelTransform.of(-11.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        bone18.addChild("bone19", ModelPartBuilder.create().uv(0, 12).cuboid(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(-11.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        ModelPartData Leg7 = bone.addChild("Leg7", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.9452F, 0.0F));

        ModelPartData bone20 = Leg7.addChild("bone20", ModelPartBuilder.create().uv(0, 10).cuboid(-11.5F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData bone21 = bone20.addChild("bone21", ModelPartBuilder.create().uv(0, 8).cuboid(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(-0.05F)), ModelTransform.of(-11.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        bone21.addChild("bone22", ModelPartBuilder.create().uv(0, 6).cuboid(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(-11.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        ModelPartData Leg8 = bone.addChild("Leg8", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.5525F, 0.0F));

        ModelPartData bone23 = Leg8.addChild("bone23", ModelPartBuilder.create().uv(0, 4).cuboid(-11.5F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData bone24 = bone23.addChild("bone24", ModelPartBuilder.create().uv(0, 2).cuboid(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(-0.05F)), ModelTransform.of(-11.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));

        bone24.addChild("bone25", ModelPartBuilder.create().uv(0, 0).cuboid(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(-11.5F, 0.0F, 0.0F, 0.0F, 0.0F, -1.1781F));
        return TexturedModelData.of(modelData, 64, 64);
    }
    @Override
    public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        PossibleDisguisedThing thing = (PossibleDisguisedThing) entity;
        float p = MathHelper.clamp(10.0F * (1.0F - Math.abs((float)thing.getRevealMaximum() - (float)thing.getTimeUntilFinishedRevealing()) / (float)thing.getRevealMaximum()), 0, 1);

        float rotation = MathHelper.lerp(p, -(float)Math.PI / 2, 0.7854F);
        float rotation2 = MathHelper.lerp(p, -(float)Math.PI, -1.1781F);

        bone2.roll = rotation;
        bone3.roll = rotation2;
        bone4.roll = rotation2;
        bone5.roll = rotation;
        bone6.roll = rotation2;
        bone7.roll = rotation2;
        bone8.roll = rotation;
        bone9.roll = rotation2;
        bone10.roll = rotation2;
        bone11.roll = rotation;
        bone12.roll = rotation2;
        bone13.roll = rotation2;
        bone14.roll = rotation;
        bone15.roll = rotation2;
        bone16.roll = rotation2;
        bone17.roll = rotation;
        bone18.roll = rotation2;
        bone19.roll = rotation2;
        bone20.roll = rotation;
        bone21.roll = rotation2;
        bone22.roll = rotation2;
        bone23.roll = rotation;
        bone24.roll = rotation2;
        bone25.roll = rotation2;
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        bone.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}