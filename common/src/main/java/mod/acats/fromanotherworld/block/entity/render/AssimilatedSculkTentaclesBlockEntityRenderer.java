package mod.acats.fromanotherworld.block.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.block.entity.AssimilatedSculkTentaclesBlockEntity;
import mod.acats.fromanotherworld.block.entity.model.AssimilatedSculkTentacleHeadModel;
import mod.acats.fromanotherworld.block.entity.model.AssimilatedSculkTentacleSegmentModel;
import mod.acats.fromanotherworld.block.entity.model.AssimilatedSculkTentacleSpiderModel;
import mod.acats.fromanotherworld.block.entity.model.AssimilatedSculkTentaclesBlockEntityModel;
import mod.acats.fromanotherworld.registry.BlockRegistry;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.constant.DataTickets;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.renderer.GeoBlockRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class AssimilatedSculkTentaclesBlockEntityRenderer extends GeoBlockRenderer<AssimilatedSculkTentaclesBlockEntity> {
    private final AssimilatedSculkTentacleSegmentModel segmentModel;
    private final AssimilatedSculkTentacleSpiderModel spiderModel;
    private final AssimilatedSculkTentacleHeadModel headModel;
    public AssimilatedSculkTentaclesBlockEntityRenderer() {
        super(new AssimilatedSculkTentaclesBlockEntityModel());
        this.segmentModel = new AssimilatedSculkTentacleSegmentModel();
        this.spiderModel = new AssimilatedSculkTentacleSpiderModel();
        this.headModel = new AssimilatedSculkTentacleHeadModel();
    }

    @Override
    public void preRender(PoseStack poseStack, AssimilatedSculkTentaclesBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isReRender) {
            poseStack.translate(0, -0.01f, 0);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public boolean shouldRender(AssimilatedSculkTentaclesBlockEntity blockEntity, Vec3 vec3) {
        if (!BlockRegistry.ASSIMILATED_SCULK_TENTACLES.get().revealed(blockEntity.getBlockState())) {
            return false;
        }
        return super.shouldRender(blockEntity, vec3);
    }

    private static final RenderType SPIDER;
    private static final RenderType SEGMENT;
    private static final RenderType HEAD;
    private static final RenderType SPIDER_GLOW;
    private static final RenderType SEGMENT_GLOW;
    private static final RenderType HEAD_GLOW;

    @Override
    public void actuallyRender(PoseStack poseStack,
                               AssimilatedSculkTentaclesBlockEntity animatable,
                               BakedGeoModel model,
                               RenderType renderType,
                               MultiBufferSource bufferSource,
                               VertexConsumer buffer,
                               boolean isReRender,
                               float partialTick,
                               int packedLight,
                               int packedOverlay,
                               float red,
                               float green,
                               float blue,
                               float alpha) {

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        if (isReRender) {
            return;
        }

        for (int i = 0; i < animatable.tentacle.segments.size(); i++) {

            BakedGeoModel bakedGeoModel = segmentModel.getBakedModel(segmentModel.getModelResource(animatable));
            RenderType renderType1 = SEGMENT;
            RenderType renderType2 = SEGMENT_GLOW;
            if (i == 0) {
                bakedGeoModel = headModel.getBakedModel(headModel.getModelResource(animatable));
                renderType1 = HEAD;
                renderType2 = HEAD_GLOW;
            } else if (i >= animatable.tentacle.segments.size() - 5) {
                bakedGeoModel = spiderModel.getBakedModel(spiderModel.getModelResource(animatable));
                renderType1 = SPIDER;
                renderType2 = SPIDER_GLOW;
            }

            GeoBone bone = bakedGeoModel.topLevelBones().get(0);
            Vec3 pos = lerpedPos(partialTick, i, animatable)
                    .subtract(
                            animatable.getBlockPos().getX(),
                            animatable.getBlockPos().getY(),
                            animatable.getBlockPos().getZ()
                    ).add(-0.5D, 0, -0.5D)
                    .multiply(-16, 16, 16);

            bone.setPosX((float) pos.x());
            bone.setPosY((float) pos.y());
            bone.setPosZ((float) pos.z());
            bone.setRotX(-lerpedPitch(partialTick, i, animatable));
            bone.setRotY(-lerpedYaw(partialTick, i, animatable) + (float)Math.PI);

            this.reRender(bakedGeoModel, poseStack, bufferSource, animatable, renderType1, bufferSource.getBuffer(renderType1), partialTick, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            this.reRender(bakedGeoModel, poseStack, bufferSource, animatable, renderType2, bufferSource.getBuffer(renderType2), partialTick, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private void tentacleAnimations(float partialTick) {
        AnimationState<AssimilatedSculkTentaclesBlockEntity> animationState = new AnimationState<>(animatable, 0, 0, partialTick, false);
        long instanceId = getInstanceId(animatable);

        animationState.setData(DataTickets.TICK, animatable.getTick(animatable));
        animationState.setData(DataTickets.BLOCK_ENTITY, animatable);
        this.segmentModel.addAdditionalStateData(animatable, instanceId, animationState::setData);
        this.spiderModel.addAdditionalStateData(animatable, instanceId, animationState::setData);
        this.segmentModel.handleAnimations(animatable, instanceId, animationState);
        this.spiderModel.handleAnimations(animatable, instanceId, animationState);
    }

    private float lerpedPitch(float partialTick, int segment, AssimilatedSculkTentaclesBlockEntity blockEntity) {
        return Mth.lerp(partialTick, blockEntity.prevPitch[segment], blockEntity.pitch[segment]);
    }

    private float lerpedYaw(float partialTick, int segment, AssimilatedSculkTentaclesBlockEntity blockEntity) {
        return Mth.lerp(partialTick, blockEntity.prevYaw[segment], blockEntity.yaw[segment]);
    }

    private Vec3 lerpedPos(float partialTick, int segment, AssimilatedSculkTentaclesBlockEntity blockEntity) {
        return new Vec3(
                Mth.lerp(partialTick, blockEntity.prevX[segment], blockEntity.x[segment]),
                Mth.lerp(partialTick, blockEntity.prevY[segment], blockEntity.y[segment]),
                Mth.lerp(partialTick, blockEntity.prevZ[segment], blockEntity.z[segment])
        );
    }

    static {
        SPIDER = RenderType.entityTranslucent(new ResourceLocation(
                FromAnotherWorld.MOD_ID,
                "textures/block/sculk/assimilated_sculk_tentacle_spider.png"
        ));
        SEGMENT = RenderType.entityTranslucent(new ResourceLocation(
                FromAnotherWorld.MOD_ID,
                "textures/block/sculk/assimilated_sculk_tentacle_segment.png"
        ));
        HEAD = RenderType.entityTranslucent(new ResourceLocation(
                FromAnotherWorld.MOD_ID,
                "textures/block/sculk/assimilated_sculk_tentacle_head.png"
        ));

        SPIDER_GLOW = RenderType.eyes(new ResourceLocation(
                FromAnotherWorld.MOD_ID,
                "textures/block/sculk/assimilated_sculk_tentacle_spider_glowmask.png"
        ));
        SEGMENT_GLOW = RenderType.eyes(new ResourceLocation(
                FromAnotherWorld.MOD_ID,
                "textures/block/sculk/assimilated_sculk_tentacle_segment_glowmask.png"
        ));
        HEAD_GLOW = RenderType.eyes(new ResourceLocation(
                FromAnotherWorld.MOD_ID,
                "textures/block/sculk/assimilated_sculk_tentacle_head_glowmask.png"
        ));
    }
}
