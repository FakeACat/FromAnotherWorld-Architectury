package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.CrawlerEntityModel;
import acats.fromanotherworld.entity.thing.resultant.CrawlerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class CrawlerEntityRenderer extends AbsorberThingEntityRenderer<CrawlerEntity> {

    public CrawlerEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new CrawlerEntityModel());
        this.shadowRadius = 0.4F;
    }

    @Override
    public void preRender(PoseStack poseStack, CrawlerEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isReRender){
            float g = 0.9375F;
            poseStack.scale(g, g, g);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
