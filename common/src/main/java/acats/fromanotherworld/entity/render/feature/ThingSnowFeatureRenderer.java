package acats.fromanotherworld.entity.render.feature;

import acats.fromanotherworld.entity.thing.AbstractThingEntity;
import acats.fromanotherworld.entity.texture.ThingOverlayTexture;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class ThingSnowFeatureRenderer<T extends AbstractThingEntity> extends GeoRenderLayer<T> {
    public ThingSnowFeatureRenderer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack poseStack, T animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable.getCold() > 0){
            RenderLayer overlayLayer = ThingOverlayTexture.getSnowOverlayRenderLayer(this.getGeoModel().getTextureResource(animatable));
            renderer.reRender(bakedModel, poseStack, bufferSource, animatable, overlayLayer, bufferSource.getBuffer(overlayLayer), partialTick, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, animatable.getCold());
        }
    }
}
