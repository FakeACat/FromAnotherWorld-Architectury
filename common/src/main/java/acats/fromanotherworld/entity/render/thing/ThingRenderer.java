package acats.fromanotherworld.entity.render.thing;

import acats.fromanotherworld.entity.thing.Thing;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import acats.fromanotherworld.entity.render.feature.ThingDamagedFeatureRenderer;
import acats.fromanotherworld.entity.render.feature.ThingSnowFeatureRenderer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class ThingRenderer<T extends Thing> extends GeoEntityRenderer<T> {
    public ThingRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
        this.addRenderLayer(new ThingDamagedFeatureRenderer<>(this));
        this.addRenderLayer(new ThingSnowFeatureRenderer<>(this));
    }

    private float lerpedClimbProgress(T entity, float partialTick){
        return Mth.lerp(partialTick, entity.climbRotateProgress, entity.nextClimbRotateProgress);
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isReRender && animatable.rotateWhenClimbing() && animatable.climbRotateProgress > 0) {
            float progress = 90 * this.lerpedClimbProgress(animatable, partialTick);
            if (this.collision(animatable, Direction.NORTH))
                poseStack.mulPose(Axis.XP.rotationDegrees(progress));
            else if (this.collision(animatable, Direction.EAST))
                poseStack.mulPose(Axis.ZP.rotationDegrees(progress));
            else if (this.collision(animatable, Direction.SOUTH))
                poseStack.mulPose(Axis.XN.rotationDegrees(progress));
            else if (this.collision(animatable, Direction.WEST))
                poseStack.mulPose(Axis.ZN.rotationDegrees(progress));
            poseStack.translate(0.0F, animatable.offsetWhenClimbing() * animatable.climbRotateProgress, 0.0F);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    protected void applyRotations(T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        if (animatable.rotateWhenClimbing() && animatable.climbRotateProgress > 0){
            float idealRotationYaw;
            if (this.collision(animatable, Direction.NORTH))
                idealRotationYaw = 180;
            else if (this.collision(animatable, Direction.EAST))
                idealRotationYaw = 270;
            else if (this.collision(animatable, Direction.SOUTH))
                idealRotationYaw = 0;
            else if (this.collision(animatable, Direction.WEST))
                idealRotationYaw = 90;
            else{
                super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
                return;
            }
            rotationYaw = Mth.rotLerp(this.lerpedClimbProgress(animatable, partialTick), rotationYaw, idealRotationYaw);
        }
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
    }

    private boolean collision(T animatable, Direction direction){
        int dist = Mth.floor(animatable.getBbWidth() / 2 + 1);
        return animatable.isColliding(animatable.blockPosition(), animatable.level().getBlockState(animatable.blockPosition().offset(direction.getNormal().multiply(dist))));
        /*if (direction == Direction.NORTH || direction == Direction.WEST)
            dist *= -1;
        Box box = animatable.getBoundingBox();
        for (int y = MathHelper.floor(box.minY); y < MathHelper.ceil(box.maxY); y++){
            if (direction == Direction.WEST || direction == Direction.EAST){
                int x = animatable.getBlockX() + dist;
                for (int z = MathHelper.floor(box.minZ); z < MathHelper.ceil(box.maxZ); z++){
                    if (animatable.collidesWithStateAtPos(animatable.getBlockPos(), animatable.world.getBlockState(new BlockPos(x, y, z))))
                        return true;
                }
            }
            else{
                int z = animatable.getBlockZ() + dist;
                for (int x = MathHelper.floor(box.minX); x < MathHelper.ceil(box.maxX); x++){
                    if (animatable.collidesWithStateAtPos(animatable.getBlockPos(), animatable.world.getBlockState(new BlockPos(x, y, z))))
                        return true;
                }
            }
        }
        return false;*/
    }
}
