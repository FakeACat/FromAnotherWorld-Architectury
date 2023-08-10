package mod.acats.fromanotherworld.entity.render.thing.growths;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.entity.interfaces.TentacleThing;
import mod.acats.fromanotherworld.entity.thing.Thing;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class Tentacle {
    private final Thing tentacler;
    private final TentacleThing tentacleThing;
    private final Vec3 offsetOnVictim;
    private final int activationProbability;
    private boolean active;
    private float extension = 0.0F;
    private Vec3 targetPos;
    private Vec3 previousTargetPos;
    private Vec3 lastTargetPos;
    private float victimWidth = 1.0F;
    private float victimHeight = 1.0F;
    public Tentacle(TentacleThing tentacleThing, int activationProbability, Vec3 offsetOnVictim){
        this.tentacleThing = tentacleThing;
        this.tentacler = (Thing) tentacleThing;
        this.targetPos = tentacler.position();
        this.previousTargetPos = this.targetPos;
        this.lastTargetPos = this.targetPos;
        this.offsetOnVictim = offsetOnVictim;
        this.activationProbability = activationProbability;
    }
    public Tentacle(TentacleThing tentacleThing, int activationProbability){
        this(tentacleThing, activationProbability, new Vec3(0.0D, 0.5D, 0.0D));
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/palmer_thing/palmer_thing_tongue.png");

    private Vec3 tentaclerOrigin(){
        return this.tentacler.position();
    }

    public void tick(@Nullable LivingEntity victim){
        if (victim != null){
            if (victim.getRandom().nextInt(activationProbability) == 0)
                this.active = true;
            if (this.active){
                this.extension = Math.min(this.extension + 0.2F, 1.0F);
                this.victimWidth = victim.getBbWidth();
                this.victimHeight = victim.getBbHeight();
                this.previousTargetPos = this.targetPos;
                this.targetPos = this.lerpedVec3d(this.extension, this.tentaclerOrigin(), victim.position());
                this.lastTargetPos = victim.position();
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

    public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, float partialTick, int packedLight){
        if (this.active){
            float l = this.tentacleThing.tentacleOriginOffset();
            matrixStack.pushPose();
            matrixStack.translate(0.0F, l, 0.0F);
            Vec3 vec3d = lerpedVictimPosition(partialTick);
            Vec3 vec3d2 = fromLerpedPosition(this.tentacler, l, partialTick);
            Vec3 vec3d3 = vec3d.subtract(vec3d2);
            float m = (float)vec3d3.length();
            vec3d3 = vec3d3.normalize();
            float n = (float)Math.acos(vec3d3.y);
            float o = (float)Math.atan2(vec3d3.z, vec3d3.x);
            matrixStack.mulPose(Axis.YP.rotationDegrees((1.5707964F - o) * 57.295776F));
            matrixStack.mulPose(Axis.XP.rotationDegrees(n * 57.295776F));
            int s = 255;
            int t = 255;
            int u = 255;
            float x = Mth.cos(2.3561945F) * 0.282F;
            float y = Mth.sin(2.3561945F) * 0.282F;
            float z = Mth.cos(0.7853982F) * 0.282F;
            float aa = Mth.sin(0.7853982F) * 0.282F;
            float ab = Mth.cos(3.926991F) * 0.282F;
            float ac = Mth.sin(3.926991F) * 0.282F;
            float ad = Mth.cos(5.4977875F) * 0.282F;
            float ae = Mth.sin(5.4977875F) * 0.282F;
            float af = Mth.cos(3.1415927F) * 0.2F;
            float ag = Mth.sin(3.1415927F) * 0.2F;
            float ah = Mth.cos(0.0F) * 0.2F;
            float ai = Mth.sin(0.0F) * 0.2F;
            float aj = Mth.cos(1.5707964F) * 0.2F;
            float ak = Mth.sin(1.5707964F) * 0.2F;
            float al = Mth.cos(4.712389F) * 0.2F;
            float am = Mth.sin(4.712389F) * 0.2F;
            float aq = -1.0F;
            float ar = m * 2.5F + aq;
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
            PoseStack.Pose entry = matrixStack.last();
            Matrix4f matrix4f = entry.pose();
            Matrix3f matrix3f = entry.normal();
            vertex(vertexConsumer, matrix4f, matrix3f, af, m, ag, s, t, u, 0.4999F, ar, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, af, 0.0F, ag, s, t, u, 0.4999F, aq, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, ah, 0.0F, ai, s, t, u, 0.0F, aq, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, ah, m, ai, s, t, u, 0.0F, ar, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, aj, m, ak, s, t, u, 0.4999F, ar, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, aj, 0.0F, ak, s, t, u, 0.4999F, aq, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, al, 0.0F, am, s, t, u, 0.0F, aq, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, al, m, am, s, t, u, 0.0F, ar, packedLight);
            float as = 0.0F;
            if (this.tentacler.tickCount % 2 == 0) {
                as = 0.5F;
            }

            vertex(vertexConsumer, matrix4f, matrix3f, x, m, y, s, t, u, 0.5F, as + 0.5F, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, z, m, aa, s, t, u, 1.0F, as + 0.5F, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, ad, m, ae, s, t, u, 1.0F, as, packedLight);
            vertex(vertexConsumer, matrix4f, matrix3f, ab, m, ac, s, t, u, 0.5F, as, packedLight);
            matrixStack.popPose();
        }
    }

    public boolean shouldRender(Frustum frustum){
        if (this.active){
            Vec3 vec3d = lerpedVictimPosition(1.0F);
            Vec3 vec3d2 = this.fromLerpedPosition(this.tentacler, this.tentacleThing.tentacleOriginOffset(), 1.0F);
            return frustum.isVisible(new AABB(vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y, vec3d.z));
        }
        return false;
    }

    private void vertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, float x, float y, float z, int red, int green, int blue, float u, float v, int light) {
        vertexConsumer.vertex(positionMatrix, x, y, z).color(red, green, blue, 255).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private Vec3 fromLerpedPosition(LivingEntity entity, double yOffset, float delta) {
        double d = Mth.lerp(delta, entity.xOld, entity.getX());
        double e = Mth.lerp(delta, entity.yOld, entity.getY()) + yOffset;
        double f = Mth.lerp(delta, entity.zOld, entity.getZ());
        return new Vec3(d, e, f);
    }

    private Vec3 lerpedVictimPosition(float delta){
        double d = Mth.lerp(delta, previousTargetPos.x(), targetPos.x()) + offsetOnVictim.x() * victimWidth;
        double e = Mth.lerp(delta, previousTargetPos.y(), targetPos.y()) + offsetOnVictim.y() * victimHeight;
        double f = Mth.lerp(delta, previousTargetPos.z(), targetPos.z()) + offsetOnVictim.z() * victimWidth;
        return new Vec3(d, e, f);
    }

    private Vec3 lerpedVec3d(float delta, Vec3 start, Vec3 end){
        double d = Mth.lerp(delta, start.x(), end.x());
        double e = Mth.lerp(delta, start.y(), end.y());
        double f = Mth.lerp(delta, start.z(), end.z());
        return new Vec3(d, e, f);
    }
}
