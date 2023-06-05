package acats.fromanotherworld.entity.render.thing;

import acats.fromanotherworld.entity.thing.TransitionEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class TransitionEntityRenderer extends EntityRenderer<TransitionEntity> {
    public TransitionEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(TransitionEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        this.renderFakeEntity(entity.getFakeEntity(), yaw, tickDelta, matrices, vertexConsumers, light);
    }

    private <E extends Entity> void renderFakeEntity(E entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light){
        EntityRenderer<? super E> renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        renderer.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(TransitionEntity entity) {
        return null;
    }
}
