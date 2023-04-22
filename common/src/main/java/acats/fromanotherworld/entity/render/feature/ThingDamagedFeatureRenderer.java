package acats.fromanotherworld.entity.render.feature;

import acats.fromanotherworld.entity.AbstractThingEntity;
import acats.fromanotherworld.entity.texture.ThingOverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class ThingDamagedFeatureRenderer<T extends AbstractThingEntity> extends GeoRenderLayer<T> {
    public ThingDamagedFeatureRenderer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack poseStack, T animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable.getHealth() < animatable.getMaxHealth()){
            float damage = 1.0F - (animatable.getHealth() / animatable.getMaxHealth());
            RenderLayer overlayLayer = ThingOverlayTexture.getInjuredOverlayRenderLayer(this.getGeoModel().getTextureResource(animatable));
            renderer.reRender(bakedModel, poseStack, bufferSource, animatable, overlayLayer, bufferSource.getBuffer(overlayLayer), partialTick, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, damage);
        }
    }
}
