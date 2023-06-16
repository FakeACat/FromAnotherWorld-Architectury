package acats.fromanotherworld.entity.render.feature;

import acats.fromanotherworld.entity.thing.special.AlienThing;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import acats.fromanotherworld.entity.texture.ThingOverlayTexture;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoRenderer;
import mod.azure.azurelib.renderer.layer.GeoRenderLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class AlienThingOverlayFeatureRenderer<T extends AlienThing> extends GeoRenderLayer<T> {
    public AlienThingOverlayFeatureRenderer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    //Original overlay logic credit to Gigeresque: https://github.com/cybercat-mods/gigeresque
    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable.getSwitchProgress() > 0){
            float progress = animatable.getSwitchProgress2();
            RenderType overlayLayer = ThingOverlayTexture.getFleshOverlayRenderLayer(this.getGeoModel().getTextureResource(animatable));
            renderer.reRender(this.getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, overlayLayer, bufferSource.getBuffer(overlayLayer), partialTick, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, progress);
        }
    }
}
