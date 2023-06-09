package acats.fromanotherworld.entity.render.thing;

import acats.fromanotherworld.entity.thing.TransitionEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class TransitionEntityRenderer extends EntityRenderer<TransitionEntity> {
    public TransitionEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(TransitionEntity entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        this.renderFakeEntity(entity.getFakeEntity(), yaw, tickDelta, matrices, vertexConsumers, light);
    }

    private <E extends Entity> void renderFakeEntity(E entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light){
        EntityRenderer<? super E> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        renderer.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(TransitionEntity entity) {
        return null;
    }
}
