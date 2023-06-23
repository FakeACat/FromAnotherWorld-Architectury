package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.SplitFaceModel;
import acats.fromanotherworld.entity.render.thing.AbsorberThingRenderer;
import acats.fromanotherworld.entity.thing.resultant.SplitFace;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SplitFaceRenderer extends AbsorberThingRenderer<SplitFace> {
    public SplitFaceRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SplitFaceModel());
        this.shadowRadius = 0.6F;
    }

    @Override
    public void preRender(PoseStack poseStack, SplitFace animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isReRender){
            float g = 0.9375F;
            poseStack.scale(g, g, g);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
