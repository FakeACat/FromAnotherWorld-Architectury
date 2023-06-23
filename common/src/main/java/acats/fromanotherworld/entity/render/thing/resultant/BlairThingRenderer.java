package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.BlairThingModel;
import acats.fromanotherworld.entity.render.thing.AbsorberThingRenderer;
import acats.fromanotherworld.entity.thing.resultant.BlairThing;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BlairThingRenderer extends AbsorberThingRenderer<BlairThing> {
    public BlairThingRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BlairThingModel());
    }

    @Override
    public void preRender(PoseStack poseStack, BlairThing animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isReRender){
            float scale = 1.0F + 0.25F * animatable.getTier();
            poseStack.scale(scale, scale, scale);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
