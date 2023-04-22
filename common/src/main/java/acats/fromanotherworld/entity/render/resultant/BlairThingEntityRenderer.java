package acats.fromanotherworld.entity.render.resultant;

import acats.fromanotherworld.entity.model.resultant.BlairThingEntityModel;
import acats.fromanotherworld.entity.render.AbstractThingEntityRenderer;
import acats.fromanotherworld.entity.resultant.BlairThingEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class BlairThingEntityRenderer extends AbstractThingEntityRenderer<BlairThingEntity> {
    public BlairThingEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new BlairThingEntityModel());
    }

    @Override
    public void preRender(MatrixStack poseStack, BlairThingEntity animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isReRender){
            float scale = 1.0F + 0.25F * animatable.getTier();
            poseStack.scale(scale, scale, scale);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
