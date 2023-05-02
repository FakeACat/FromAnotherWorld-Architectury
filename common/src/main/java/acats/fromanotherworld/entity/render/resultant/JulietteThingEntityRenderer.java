package acats.fromanotherworld.entity.render.resultant;

import acats.fromanotherworld.entity.model.resultant.JulietteThingEntityModel;
import acats.fromanotherworld.entity.render.AbstractThingEntityRenderer;
import acats.fromanotherworld.entity.resultant.JulietteThingEntity;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class JulietteThingEntityRenderer extends AbstractThingEntityRenderer<JulietteThingEntity> {
    public JulietteThingEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new JulietteThingEntityModel());
        this.shadowRadius = 0.4F;
    }

    @Override
    public void preRender(MatrixStack poseStack, JulietteThingEntity animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isReRender){
            float g = 0.9375F;
            poseStack.scale(g, g, g);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
