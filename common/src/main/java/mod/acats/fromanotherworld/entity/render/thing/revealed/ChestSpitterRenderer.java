package mod.acats.fromanotherworld.entity.render.thing.revealed;

import mod.acats.fromanotherworld.entity.model.thing.growths.TentacleSegmentModel;
import mod.acats.fromanotherworld.entity.model.thing.revealed.ChestSpitterModel;
import mod.acats.fromanotherworld.entity.render.thing.ThingRenderer;
import mod.acats.fromanotherworld.entity.thing.revealed.ChestSpitter;
import com.mojang.blaze3d.vertex.PoseStack;
import mod.acats.fromanotherworld.registry.client.ClientEntityRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ChestSpitterRenderer extends ThingRenderer<ChestSpitter> {
    private final TentacleSegmentModel tentacleSegmentModel;
    public ChestSpitterRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ChestSpitterModel());
        this.tentacleSegmentModel = new TentacleSegmentModel(ctx.getModelSet().bakeLayer(ClientEntityRegistry.TENTACLE_SEGMENT_MODEL_LAYER));
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(ChestSpitter entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        entity.tentacleMass.render(tentacleSegmentModel, poseStack, bufferSource, packedLight, partialTick);
    }
}
