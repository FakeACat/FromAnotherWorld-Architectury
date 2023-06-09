package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.PalmerThingEntityModel;
import acats.fromanotherworld.entity.thing.resultant.PalmerThingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PalmerThingEntityRenderer extends AbsorberThingEntityRenderer<PalmerThingEntity> {

    public PalmerThingEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PalmerThingEntityModel());
        this.shadowRadius = 0.4F;
    }

    @Override
    public boolean shouldRender(PalmerThingEntity entity, Frustum frustum, double x, double y, double z) {
        return super.shouldRender(entity, frustum, x, y, z) || entity.getTongue().shouldRender(frustum);
    }

    @Override
    public void render(PalmerThingEntity entity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
        entity.getTongue().render(matrixStack, vertexConsumerProvider, g, i);
    }

    @Override
    public void preRender(PoseStack poseStack, PalmerThingEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isReRender){
            float g = 0.9375F;
            poseStack.scale(g, g, g);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
