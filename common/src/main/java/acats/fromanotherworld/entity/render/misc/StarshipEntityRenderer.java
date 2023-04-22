package acats.fromanotherworld.entity.render.misc;

import acats.fromanotherworld.entity.misc.StarshipEntity;
import acats.fromanotherworld.entity.model.misc.StarshipEntityModel;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StarshipEntityRenderer extends GeoEntityRenderer<StarshipEntity> {
    public StarshipEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new StarshipEntityModel());
    }

    @Override
    public void preRender(MatrixStack poseStack, StarshipEntity animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(animatable.isOnGround() ? 15.0F : 90.0F));
        float scale = 1.5F;
        poseStack.scale(scale, scale, scale);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    protected float getDeathMaxRotation(StarshipEntity animatable) {
        return 0.0F;
    }
}
