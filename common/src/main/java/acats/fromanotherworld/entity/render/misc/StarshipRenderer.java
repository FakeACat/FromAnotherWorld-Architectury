package acats.fromanotherworld.entity.render.misc;

import acats.fromanotherworld.entity.misc.StarshipEntity;
import acats.fromanotherworld.entity.model.misc.StarshipModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class StarshipRenderer extends GeoEntityRenderer<StarshipEntity> {
    public StarshipRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new StarshipModel());
    }

    @Override
    public void preRender(PoseStack poseStack, StarshipEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.mulPose(Axis.XP.rotationDegrees(animatable.onGround() ? 15.0F : 90.0F));
        float scale = 1.5F;
        poseStack.scale(scale, scale, scale);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    protected float getDeathMaxRotation(StarshipEntity animatable) {
        return 0.0F;
    }
}
