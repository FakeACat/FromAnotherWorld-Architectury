package mod.acats.fromanotherworld.entity.render.thing.revealed;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.acats.fromanotherworld.entity.model.thing.revealed.VineTentaclesModel;
import mod.acats.fromanotherworld.entity.thing.revealed.VineTentacles;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.core.object.Color;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class VineTentaclesRenderer extends GeoEntityRenderer<VineTentacles> {
    public VineTentaclesRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VineTentaclesModel());
        this.shadowRadius = 0.0F;
    }

    @Override
    public Color getRenderColor(VineTentacles animatable, float partialTick, int packedLight) {
        return Color.ofOpaque(BiomeColors.getAverageGrassColor(animatable.level(), animatable.blockPosition()));
    }

    @Override
    protected float getDeathMaxRotation(VineTentacles animatable) {
        return 0.0F;
    }

    @Override
    public void preRender(PoseStack poseStack, VineTentacles animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float f = animatable.lerpedExitProgress(partialTick);
        if (!isReRender && f > -1) {
            float yOffset = -f / 10;
            poseStack.translate(0.0F, yOffset, 0.0F);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
