package acats.fromanotherworld.entity.render.thing;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.interfaces.TentacleThing;
import acats.fromanotherworld.entity.thing.ThingEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class Tentacle {
    private final ThingEntity tentacler;
    private final TentacleThing tentacleThing;
    private final Vec3d offsetOnVictim;
    private final int activationProbability;
    private boolean active;
    private float extension = 0.0F;
    private Vec3d targetPos;
    private Vec3d previousTargetPos;
    private Vec3d lastTargetPos;
    private float victimWidth = 1.0F;
    private float victimHeight = 1.0F;
    public Tentacle(TentacleThing tentacleThing, int activationProbability, Vec3d offsetOnVictim){
        this.tentacleThing = tentacleThing;
        this.tentacler = (ThingEntity) tentacleThing;
        this.targetPos = tentacler.getPos();
        this.previousTargetPos = this.targetPos;
        this.lastTargetPos = this.targetPos;
        this.offsetOnVictim = offsetOnVictim;
        this.activationProbability = activationProbability;
    }
    public Tentacle(TentacleThing tentacleThing, int activationProbability){
        this(tentacleThing, activationProbability, new Vec3d(0.0D, 0.5D, 0.0D));
    }

    private static final Identifier TEXTURE = new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/palmer_thing/palmer_thing_tongue.png");

    private Vec3d tentaclerOrigin(){
        return this.tentacler.getPos();
    }

    public void tick(@Nullable LivingEntity victim){
        if (victim != null){
            if (victim.getRandom().nextInt(activationProbability) == 0)
                this.active = true;
            if (this.active){
                this.extension = Math.min(this.extension + 0.2F, 1.0F);
                this.victimWidth = victim.getWidth();
                this.victimHeight = victim.getHeight();
                this.previousTargetPos = this.targetPos;
                this.targetPos = this.lerpedVec3d(this.extension, this.tentaclerOrigin(), victim.getPos());
                this.lastTargetPos = victim.getPos();
                return;
            }
        }
        this.extension = Math.max(this.extension - 0.2F, 0.0F);
        this.previousTargetPos = this.targetPos;
        if (this.extension == 0){
            this.active = false;
            this.targetPos = this.tentaclerOrigin();
        }
        else{
            this.targetPos = this.lerpedVec3d(this.extension, this.tentaclerOrigin(), lastTargetPos);
        }
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, float partialTick, int packedLight){
        if (this.active){
            float l = this.tentacleThing.tentacleOriginOffset();
            matrixStack.push();
            matrixStack.translate(0.0F, l, 0.0F);
            Vec3d vec3d = lerpedVictimPosition(partialTick);
            Vec3d vec3d2 = fromLerpedPosition(this.tentacler, l, partialTick);
            Vec3d vec3d3 = vec3d.subtract(vec3d2);
            float m = (float)vec3d3.length();
            vec3d3 = vec3d3.normalize();
            float n = (float)Math.acos(vec3d3.y);
            float o = (float)Math.atan2(vec3d3.z, vec3d3.x);
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((1.5707964F - o) * 57.295776F));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(n * 57.295776F));
            int s = 255;
            int t = 255;
            int u = 255;
            float x = MathHelper.cos(2.3561945F) * 0.282F;
            float y = MathHelper.sin(2.3561945F) * 0.282F;
            float z = MathHelper.cos(0.7853982F) * 0.282F;
            float aa = MathHelper.sin(0.7853982F) * 0.282F;
            float ab = MathHelper.cos(3.926991F) * 0.282F;
            float ac = MathHelper.sin(3.926991F) * 0.282F;
            float ad = MathHelper.cos(5.4977875F) * 0.282F;
            float ae = MathHelper.sin(5.4977875F) * 0.282F;
            float af = MathHelper.cos(3.1415927F) * 0.2F;
            float ag = MathHelper.sin(3.1415927F) * 0.2F;
            float ah = MathHelper.cos(0.0F) * 0.2F;
            float ai = MathHelper.sin(0.0F) * 0.2F;
            float aj = MathHelper.cos(1.5707964F) * 0.2F;
            float ak = MathHelper.sin(1.5707964F) * 0.2F;
            float al = MathHelper.cos(4.712389F) * 0.2F;
            float am = MathHelper.sin(4.712389F) * 0.2F;
            float aq = -1.0F;
            float ar = m * 2.5F + aq;
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
            MatrixStack.Entry entry = matrixStack.peek();
            Matrix4f matrix4f = entry.getPositionMatrix();
            Matrix3f matrix3f = entry.getNormalMatrix();
            vertex(vertexConsumer, matrix4f, matrix3f, af, m, ag, s, t, u, 0.4999F, ar, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, af, 0.0F, ag, s, t, u, 0.4999F, aq, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, ah, 0.0F, ai, s, t, u, 0.0F, aq, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, ah, m, ai, s, t, u, 0.0F, ar, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, aj, m, ak, s, t, u, 0.4999F, ar, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, aj, 0.0F, ak, s, t, u, 0.4999F, aq, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, al, 0.0F, am, s, t, u, 0.0F, aq, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, al, m, am, s, t, u, 0.0F, ar, packedLight);
            float as = 0.0F;
            if (this.tentacler.age % 2 == 0) {
                as = 0.5F;
            }

            vertex(vertexConsumer, matrix4f, matrix3f, x, m, y, s, t, u, 0.5F, as + 0.5F, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, z, m, aa, s, t, u, 1.0F, as + 0.5F, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, ad, m, ae, s, t, u, 1.0F, as, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, ab, m, ac, s, t, u, 0.5F, as, packedLight);
            matrixStack.pop();
        }
    }

    public boolean shouldRender(Frustum frustum){
        if (this.active){
            Vec3d vec3d = lerpedVictimPosition(1.0F);
            Vec3d vec3d2 = this.fromLerpedPosition(this.tentacler, this.tentacleThing.tentacleOriginOffset(), 1.0F);
            return frustum.isVisible(new Box(vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y, vec3d.z));
        }
        return false;
    }

    private void vertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, float x, float y, float z, int red, int green, int blue, float u, float v, int light) {
        vertexConsumer.vertex(positionMatrix, x, y, z).color(red, green, blue, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
    }

    private Vec3d fromLerpedPosition(LivingEntity entity, double yOffset, float delta) {
        double d = MathHelper.lerp(delta, entity.lastRenderX, entity.getX());
        double e = MathHelper.lerp(delta, entity.lastRenderY, entity.getY()) + yOffset;
        double f = MathHelper.lerp(delta, entity.lastRenderZ, entity.getZ());
        return new Vec3d(d, e, f);
    }

    private Vec3d lerpedVictimPosition(float delta){
        double d = MathHelper.lerp(delta, previousTargetPos.getX(), targetPos.getX()) + offsetOnVictim.getX() * victimWidth;
        double e = MathHelper.lerp(delta, previousTargetPos.getY(), targetPos.getY()) + offsetOnVictim.getY() * victimHeight;
        double f = MathHelper.lerp(delta, previousTargetPos.getZ(), targetPos.getZ()) + offsetOnVictim.getZ() * victimWidth;
        return new Vec3d(d, e, f);
    }

    private Vec3d lerpedVec3d(float delta, Vec3d start, Vec3d end){
        double d = MathHelper.lerp(delta, start.getX(), end.getX());
        double e = MathHelper.lerp(delta, start.getY(), end.getY());
        double f = MathHelper.lerp(delta, start.getZ(), end.getZ());
        return new Vec3d(d, e, f);
    }
}
