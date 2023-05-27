package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.model.thing.resultant.PalmerThingEntityModel;
import acats.fromanotherworld.entity.render.thing.ThingEntityRenderer;
import acats.fromanotherworld.entity.thing.resultant.PalmerThingEntity;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class PalmerThingEntityRenderer extends ThingEntityRenderer<PalmerThingEntity> {

    private static final Identifier TEXTURE = new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/palmer_thing/palmer_thing_tongue.png");

    public PalmerThingEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new PalmerThingEntityModel());
        this.shadowRadius = 0.4F;
    }

    @Override
    public boolean shouldRender(PalmerThingEntity entity, Frustum frustum, double x, double y, double z) {
        if (super.shouldRender(entity, frustum, x, y, z)) {
            return true;
        } else {
            if (entity.targetGrabbed()) {
                LivingEntity livingEntity = entity.getTarget();
                if (livingEntity != null) {
                    Vec3d vec3d = this.fromLerpedPosition(livingEntity, (double)livingEntity.getHeight() * 0.5, 1.0F);
                    Vec3d vec3d2 = this.fromLerpedPosition(entity, entity.getStandingEyeHeight(), 1.0F);
                    return frustum.isVisible(new Box(vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y, vec3d.z));
                }
            }
            return false;
        }
    }

    private Vec3d fromLerpedPosition(LivingEntity entity, double yOffset, float delta) {
        double d = MathHelper.lerp(delta, entity.lastRenderX, entity.getX());
        double e = MathHelper.lerp(delta, entity.lastRenderY, entity.getY()) + yOffset;
        double f = MathHelper.lerp(delta, entity.lastRenderZ, entity.getZ());
        return new Vec3d(d, e, f);
    }

    @Override
    public void render(PalmerThingEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);

        if (entity.targetGrabbed()){
            LivingEntity livingEntity = (LivingEntity) entity.world.getEntityById(entity.getTargetId());
            if (livingEntity != null) {
                float j = (float)entity.world.getTime() + g;
                float k = j * 0.5F % 1.0F;
                float l = entity.getStandingEyeHeight();
                matrixStack.push();
                matrixStack.translate(0.0F, l, 0.0F);
                Vec3d vec3d = this.fromLerpedPosition(livingEntity, (double)livingEntity.getHeight() * 0.5, g);
                Vec3d vec3d2 = this.fromLerpedPosition(entity, l, g);
                Vec3d vec3d3 = vec3d.subtract(vec3d2);
                float m = (float)(vec3d3.length() + 1.0);
                vec3d3 = vec3d3.normalize();
                float n = (float)Math.acos(vec3d3.y);
                float o = (float)Math.atan2(vec3d3.z, vec3d3.x);
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((1.5707964F - o) * 57.295776F));
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(n * 57.295776F));
                float q = j * 0.05F * -1.5F;
                int s = 255;
                int t = 255;
                int u = 255;
                float x = MathHelper.cos(q + 2.3561945F) * 0.282F;
                float y = MathHelper.sin(q + 2.3561945F) * 0.282F;
                float z = MathHelper.cos(q + 0.7853982F) * 0.282F;
                float aa = MathHelper.sin(q + 0.7853982F) * 0.282F;
                float ab = MathHelper.cos(q + 3.926991F) * 0.282F;
                float ac = MathHelper.sin(q + 3.926991F) * 0.282F;
                float ad = MathHelper.cos(q + 5.4977875F) * 0.282F;
                float ae = MathHelper.sin(q + 5.4977875F) * 0.282F;
                float af = MathHelper.cos(q + 3.1415927F) * 0.2F;
                float ag = MathHelper.sin(q + 3.1415927F) * 0.2F;
                float ah = MathHelper.cos(q + 0.0F) * 0.2F;
                float ai = MathHelper.sin(q + 0.0F) * 0.2F;
                float aj = MathHelper.cos(q + 1.5707964F) * 0.2F;
                float ak = MathHelper.sin(q + 1.5707964F) * 0.2F;
                float al = MathHelper.cos(q + 4.712389F) * 0.2F;
                float am = MathHelper.sin(q + 4.712389F) * 0.2F;
                float aq = -1.0F + k;
                float ar = m * 2.5F + aq;
                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
                MatrixStack.Entry entry = matrixStack.peek();
                Matrix4f matrix4f = entry.getPositionMatrix();
                Matrix3f matrix3f = entry.getNormalMatrix();
                vertex(vertexConsumer, matrix4f, matrix3f, af, m, ag, s, t, u, 0.4999F, ar, i);
                vertex(vertexConsumer, matrix4f, matrix3f, af, 0.0F, ag, s, t, u, 0.4999F, aq, i);
                vertex(vertexConsumer, matrix4f, matrix3f, ah, 0.0F, ai, s, t, u, 0.0F, aq, i);
                vertex(vertexConsumer, matrix4f, matrix3f, ah, m, ai, s, t, u, 0.0F, ar, i);
                vertex(vertexConsumer, matrix4f, matrix3f, aj, m, ak, s, t, u, 0.4999F, ar, i);
                vertex(vertexConsumer, matrix4f, matrix3f, aj, 0.0F, ak, s, t, u, 0.4999F, aq, i);
                vertex(vertexConsumer, matrix4f, matrix3f, al, 0.0F, am, s, t, u, 0.0F, aq, i);
                vertex(vertexConsumer, matrix4f, matrix3f, al, m, am, s, t, u, 0.0F, ar, i);
                float as = 0.0F;
                if (entity.age % 2 == 0) {
                    as = 0.5F;
                }

                vertex(vertexConsumer, matrix4f, matrix3f, x, m, y, s, t, u, 0.5F, as + 0.5F, i);
                vertex(vertexConsumer, matrix4f, matrix3f, z, m, aa, s, t, u, 1.0F, as + 0.5F, i);
                vertex(vertexConsumer, matrix4f, matrix3f, ad, m, ae, s, t, u, 1.0F, as, i);
                vertex(vertexConsumer, matrix4f, matrix3f, ab, m, ac, s, t, u, 0.5F, as, i);
                matrixStack.pop();
            }
        }
    }
    private static void vertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, float x, float y, float z, int red, int green, int blue, float u, float v, int light) {
        vertexConsumer.vertex(positionMatrix, x, y, z).color(red, green, blue, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
    }

    @Override
    public void preRender(MatrixStack poseStack, PalmerThingEntity animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isReRender){
            float g = 0.9375F;
            poseStack.scale(g, g, g);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
