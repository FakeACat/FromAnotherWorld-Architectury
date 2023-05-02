package acats.fromanotherworld.entity.render.feature;

import acats.fromanotherworld.entity.special.AlienThingEntity;
import acats.fromanotherworld.entity.texture.ThingOverlayTexture;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class AlienThingOverlayFeatureRenderer<T extends AlienThingEntity> extends GeoRenderLayer<T> {
    public AlienThingOverlayFeatureRenderer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    //Original overlay logic credit to Gigeresque: https://github.com/cybercat-mods/gigeresque
    @Override
    public void render(MatrixStack poseStack, T animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable.getSwitchProgress() > 0){
            float progress = animatable.getSwitchProgress2();
            RenderLayer overlayLayer = ThingOverlayTexture.getFleshOverlayRenderLayer(this.getGeoModel().getTextureResource(animatable));
            renderer.reRender(this.getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, overlayLayer, bufferSource.getBuffer(overlayLayer), partialTick, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, progress);
        }
    }
}
