package mod.acats.fromanotherworld.entity.render.thing;

import mod.acats.fromanotherworld.entity.model.thing.growths.TentacleSegmentModel;
import mod.acats.fromanotherworld.entity.thing.TransitionEntity;
import mod.acats.fromanotherworld.registry.EntityRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class TransitionEntityRenderer extends EntityRenderer<TransitionEntity> {
    private final TentacleSegmentModel tentacleSegmentModel;
    public TransitionEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.tentacleSegmentModel = new TentacleSegmentModel(ctx.getModelSet().bakeLayer(EntityRegistry.tentacleSegmentModelLayer));
    }

    @Override
    public void render(TransitionEntity entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        if (entity.shouldRenderFakeEntity())
            this.renderFakeEntity(entity.getFakeEntity(), yaw, tickDelta, matrices, vertexConsumers, light);

        entity.tentacleMass.render(tentacleSegmentModel, matrices, vertexConsumers, light, tickDelta);
    }

    private <E extends Entity> void renderFakeEntity(E entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light){
        EntityRenderer<? super E> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        renderer.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    private static final ResourceLocation PIG_LOCATION = new ResourceLocation("textures/entity/pig/pig.png");

    @Override
    public @NotNull ResourceLocation getTextureLocation(TransitionEntity entity) {
        return PIG_LOCATION;
    }
}
