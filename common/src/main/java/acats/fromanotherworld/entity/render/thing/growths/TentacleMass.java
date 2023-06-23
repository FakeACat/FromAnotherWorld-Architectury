package acats.fromanotherworld.entity.render.thing.growths;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.model.thing.growths.TentacleSegmentModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class TentacleMass {
    public static final ResourceLocation TEXTURE = new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/growths/tentacle_segment.png");
    private static final float SEGMENT_LENGTH = 0.4375F;
    private final List<Tentacle> tentacles = new ArrayList<>();
    private final Level level;
    public TentacleMass(Level level, int tentacleCount, int tentacleSegments, float scale, float scaleMultiplier){
        this.scale = scale;
        this.level = level;
        for (int i = 0; i < tentacleCount; i++){
            tentacles.add(i, new Tentacle(tentacleSegments, scaleMultiplier));
        }
        this.rootYOffset = 0.0F;
        this.progress = 0;
    }
    public float scale;
    public float rootYOffset;
    private int progress;
    public void tick(){
        this.progress++;
        for (Tentacle tentacle:
             tentacles) {
            tentacle.tick();
        }
    }
    public void render(TentacleSegmentModel model, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, float tickDelta){
        for (Tentacle tentacle:
                tentacles) {
            tentacle.render(model, poseStack, multiBufferSource, light, tickDelta);
        }
    }

    private class Tentacle {
        private final List<TentacleSegment> tentacleSegments = new ArrayList<>();
        private final float baseXRot;
        private final float baseYRot;
        private final float scaleMultiplier;
        private final float rotDesyncX;
        private final float rotDesyncY;
        private final float rotMultX;
        private final float rotMultY;
        private Tentacle(int segments, float scaleMultiplier){
            this.baseXRot = this.randomRotation();
            this.baseYRot = this.randomRotation();
            this.rotDesyncX = this.randomRotation();
            this.rotDesyncY = this.randomRotation();
            this.rotMultX = level.getRandom().nextFloat() + 0.5F;
            this.rotMultY = level.getRandom().nextFloat() + 0.5F;
            for (int i = 0; i < segments; i++){
                tentacleSegments.add(i, new TentacleSegment());
            }
            this.scaleMultiplier = scaleMultiplier;
        }
        private float randomRotation(){
            return level.getRandom().nextFloat() * (float)Math.PI * 2.0F;
        }
        private void tick(){
            TentacleSegment previous = null;
            for (TentacleSegment segment:
                    tentacleSegments) {

                segment.prevXRot = segment.xRot;
                segment.prevYRot = segment.yRot;
                segment.prevScale = segment.scale;

                segment.prevOffsetX = segment.offsetX;
                segment.prevOffsetY = segment.offsetY;
                segment.prevOffsetZ = segment.offsetZ;

                if (previous == null){
                    segment.xRot = this.baseXRot;
                    segment.yRot = this.baseYRot;
                    segment.scale = scale;
                    Vec3 offset = Vec3.directionFromRotation((float)Math.toDegrees(segment.xRot), (float)Math.toDegrees(segment.yRot));
                    segment.offsetX = (float)offset.x() * SEGMENT_LENGTH * segment.scale;
                    segment.offsetY = (float)offset.y() * SEGMENT_LENGTH * segment.scale;
                    segment.offsetZ = (float)offset.z() * SEGMENT_LENGTH * segment.scale;
                }
                else{
                    segment.xRot = previous.xRot + (float)Math.sin(this.rotDesyncX + this.rotMultX * progress / 8.0F) / 4.0F;
                    segment.yRot = previous.yRot + (float)Math.cos(this.rotDesyncY + this.rotMultY * progress / 8.0F) / 4.0F;
                    Vec3 offset = Vec3.directionFromRotation((float)Math.toDegrees(segment.xRot), (float)Math.toDegrees(segment.yRot));
                    segment.scale = previous.scale * scaleMultiplier;
                    segment.offsetX = previous.offsetX + (float)offset.x() * SEGMENT_LENGTH * segment.scale;
                    segment.offsetY = previous.offsetY + (float)offset.y() * SEGMENT_LENGTH * segment.scale;
                    segment.offsetZ = previous.offsetZ + (float)offset.z() * SEGMENT_LENGTH * segment.scale;
                }
                previous = segment;
            }
        }
        private void render(TentacleSegmentModel model, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, float tickDelta){
            for (TentacleSegment segment:
                    tentacleSegments) {
                segment.render(model, poseStack, multiBufferSource, light, tickDelta);
            }
        }
        private class TentacleSegment {
            private float xRot;
            private float yRot;
            private float prevXRot;
            private float prevYRot;
            private float offsetX;
            private float offsetY;
            private float offsetZ;
            private float prevOffsetX;
            private float prevOffsetY;
            private float prevOffsetZ;
            private float scale;
            private float prevScale;
            private float getOffsetX(float tickDelta){
                return Mth.lerp(tickDelta, this.prevOffsetX, this.offsetX);
            }
            private float getOffsetY(float tickDelta){
                return Mth.lerp(tickDelta, this.prevOffsetY, this.offsetY);
            }
            private float getOffsetZ(float tickDelta){
                return Mth.lerp(tickDelta, this.prevOffsetZ, this.offsetZ);
            }
            private float getXRot(float tickDelta){
                return Mth.lerp(tickDelta, this.prevXRot, this.xRot);
            }
            private float getYRot(float tickDelta){
                return Mth.lerp(tickDelta, this.prevYRot, this.yRot);
            }
            private float getScale(float tickDelta){
                return Mth.lerp(tickDelta, this.prevScale, this.scale);
            }
            private void render(TentacleSegmentModel model, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, float tickDelta){
                poseStack.pushPose();
                VertexConsumer v = multiBufferSource.getBuffer(RenderType.entityTranslucent(TentacleMass.TEXTURE));
                model.setScale(this.getScale(tickDelta));
                model.setPosition(this.getOffsetX(tickDelta), this.getOffsetY(tickDelta) + rootYOffset, this.getOffsetZ(tickDelta));
                model.setRotation(this.getXRot(tickDelta), -this.getYRot(tickDelta), 0);
                model.renderToBuffer(poseStack, v, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                poseStack.popPose();
            }
        }
    }
}
