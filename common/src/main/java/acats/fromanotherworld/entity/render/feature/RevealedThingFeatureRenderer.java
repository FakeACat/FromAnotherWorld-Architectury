package acats.fromanotherworld.entity.render.feature;

import acats.fromanotherworld.entity.DisguisedThing;
import acats.fromanotherworld.entity.model.revealed.SpiderLegsEntityModel;
import acats.fromanotherworld.entity.texture.ThingOverlayTexture;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class RevealedThingFeatureRenderer<T extends Entity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private final SpiderLegsEntityModel spiderLegsEntityModel;

    public RevealedThingFeatureRenderer(FeatureRendererContext<T, M> context, ModelPart root) {
        super(context);
        this.spiderLegsEntityModel = new SpiderLegsEntityModel(root);
    }


    //Original overlay logic credit to Gigeresque: https://github.com/cybercat-mods/gigeresque
    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

        if (entity instanceof DisguisedThing e && !entity.isInvisible()){
            if (e.isRevealed() && entity.getHeight() <= 1.0F){
                VertexConsumer v = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(ThingOverlayTexture.FLESH_OVERLAY_TEXTURE));
                this.spiderLegsEntityModel.setAngles(entity, 0, 0, 0, 0, 0);
                this.spiderLegsEntityModel.render(matrices, v, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            }

            renderFleshOverlay(e, this.getContextModel(), this.getTexture(entity), matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
        }
    }

    public static <T extends Entity> void renderFleshOverlay(DisguisedThing e, EntityModel<T> model, Identifier texture, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch){
        if (e.isRevealed() || e.getSupercellConcentration() >= 1.0F){
            float progress;
            if (e.isRevealed()){
                if (e.getTimeUntilFinishedRevealing() < e.getRevealMaximum()){
                    progress = (float)e.getTimeUntilFinishedRevealing() / e.getRevealMaximum();
                }
                else{
                    progress = 2 - (float)e.getTimeUntilFinishedRevealing() / e.getRevealMaximum();
                }
            }
            else{
                progress = 1.0F - (1.0F - e.getSupercellConcentration() / 50) * (1.0F - e.getSupercellConcentration() / 50);
            }
            matrices.push();
            model.animateModel(entity, limbAngle, limbDistance, tickDelta);
            model.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(ThingOverlayTexture.getFleshOverlayRenderLayer(texture));
            model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, progress);
            matrices.pop();
        }
    }
}
