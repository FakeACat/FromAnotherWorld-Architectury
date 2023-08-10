package mod.acats.fromanotherworld.block.entity.render;

import mod.acats.fromanotherworld.block.entity.TunnelBlockEntity;
import mod.acats.fromanotherworld.block.entity.model.TunnelBlockEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoBlockRenderer;
import net.minecraft.client.renderer.MultiBufferSource;

public class TunnelBlockEntityRenderer extends GeoBlockRenderer<TunnelBlockEntity> {
    public TunnelBlockEntityRenderer() {
        super(new TunnelBlockEntityModel());
    }

    @Override
    public void preRender(PoseStack poseStack, TunnelBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isReRender) {
            poseStack.translate(0, -0.01f, 0);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
