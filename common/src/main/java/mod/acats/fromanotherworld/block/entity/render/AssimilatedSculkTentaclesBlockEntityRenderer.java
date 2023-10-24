package mod.acats.fromanotherworld.block.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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

    @Override
    public void actuallyRender(PoseStack poseStack, AssimilatedSculkTentaclesBlockEntity animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        if (isReRender) {
            return;
        }

        BakedGeoModel segmentBakedModel = segmentModel.getBakedModel(segmentModel.getModelResource(animatable));
        BakedGeoModel spiderBakedModel = spiderModel.getBakedModel(spiderModel.getModelResource(animatable));
        BakedGeoModel headBakedModel = headModel.getBakedModel(headModel.getModelResource(animatable));

        for (int i = 0; i < animatable.tentacle.segments.size(); i++) {
            AssimilatedSculkTentaclesBlockEntityModel geoModel = segmentModel;
            BakedGeoModel bakedGeoModel = segmentBakedModel;
            if (i == 0) {
                geoModel = headModel;
                bakedGeoModel = headBakedModel;
            } else if (i >= animatable.tentacle.segments.size() - 5) {
                geoModel = spiderModel;
                bakedGeoModel = spiderBakedModel;
            }
            poseStack.pushPose();
            VertexConsumer v = bufferSource.getBuffer(RenderType.entityTranslucent(geoModel.getTextureResource(animatable)));
            Vec3 pos = lerpedPos(partialTick, i, animatable).subtract(animatable.getBlockPos().getX(), animatable.getBlockPos().getY(), animatable.getBlockPos().getZ()).add(-0.5D, 0, -0.5D).multiply(-16, 16, 16);
            GeoBone bone = bakedGeoModel.topLevelBones().get(0);
            bone.setPosX((float) pos.x());
            bone.setPosY((float) pos.y());
            bone.setPosZ((float) pos.z());
            bone.setRotX(-lerpedPitch(partialTick, i, animatable));
            bone.setRotY(-lerpedYaw(partialTick, i, animatable) + (float)Math.PI);
            //tentacleAnimations(partialTick);
            super.actuallyRender(poseStack, animatable, bakedGeoModel, renderType, bufferSource, v, true, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

            VertexConsumer overlay = bufferSource.getBuffer(RenderType.entityTranslucent(geoModel.getGlowTextureResource()));

            super.actuallyRender(poseStack, animatable, bakedGeoModel, renderType, bufferSource, overlay, true, partialTick, 15728640, packedOverlay, 1.0F, 1.0F, 1.0F, alpha);

            poseStack.popPose();
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
}
