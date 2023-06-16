package acats.fromanotherworld.entity.render.feature;

import acats.fromanotherworld.entity.thing.Thing;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import acats.fromanotherworld.entity.texture.ThingOverlayTexture;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class ThingDamagedFeatureRenderer<T extends Thing> extends GeoRenderLayer<T> {
    public ThingDamagedFeatureRenderer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable.getHealth() < animatable.getMaxHealth()){
            float damage = 1.0F - (animatable.getHealth() / animatable.getMaxHealth());
            RenderType overlayLayer = ThingOverlayTexture.getInjuredOverlayRenderLayer(this.getGeoModel().getTextureResource(animatable));
            renderer.reRender(bakedModel, poseStack, bufferSource, animatable, overlayLayer, bufferSource.getBuffer(overlayLayer), partialTick, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, damage);
        }
    }
}
