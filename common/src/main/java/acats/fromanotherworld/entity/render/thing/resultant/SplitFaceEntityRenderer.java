package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.SplitFaceEntityModel;
import acats.fromanotherworld.entity.thing.resultant.SplitFaceEntity;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class SplitFaceEntityRenderer extends AbsorberThingEntityRenderer<SplitFaceEntity> {
    public SplitFaceEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new SplitFaceEntityModel());
        this.shadowRadius = 0.6F;
    }

    @Override
    public void preRender(MatrixStack poseStack, SplitFaceEntity animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isReRender){
            float g = 0.9375F;
            poseStack.scale(g, g, g);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
