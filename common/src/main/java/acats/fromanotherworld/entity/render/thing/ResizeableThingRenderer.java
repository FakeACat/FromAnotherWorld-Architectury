package acats.fromanotherworld.entity.render.thing;

import acats.fromanotherworld.entity.thing.ResizeableThing;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ResizeableThingRenderer<T extends ResizeableThing> extends AbsorberThingRenderer<T> {
    public ResizeableThingRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
    }

    public float originalModelScale(T thing){
        return thing.getOriginalWidth() + thing.getOriginalWidth() + thing.getOriginalHeight();
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isReRender){
            float currentScale = animatable.getBbWidth() + animatable.getBbWidth() + animatable.getBbHeight();
            float scale = currentScale / this.originalModelScale(animatable);
            poseStack.scale(scale, scale, scale);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
