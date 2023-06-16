package acats.fromanotherworld.entity.render.feature;

import acats.fromanotherworld.entity.interfaces.PossibleDisguisedThing;
import acats.fromanotherworld.entity.model.thing.revealed.SpiderLegsModel;
import acats.fromanotherworld.entity.texture.ThingOverlayTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class RevealedThingFeatureRenderer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final SpiderLegsModel spiderLegsModel;

    public RevealedThingFeatureRenderer(RenderLayerParent<T, M> context, ModelPart root) {
        super(context);
        this.spiderLegsModel = new SpiderLegsModel(root);
    }


    //Original overlay logic credit to Gigeresque: https://github.com/cybercat-mods/gigeresque
    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

        if (entity instanceof PossibleDisguisedThing e && !entity.isInvisible()){
            if (e.isRevealed() && entity.getBbHeight() <= 1.0F){
                VertexConsumer v = vertexConsumers.getBuffer(RenderType.entitySolid(ThingOverlayTexture.FLESH_OVERLAY_TEXTURE));
                this.spiderLegsModel.setupAnim(entity, 0, 0, 0, 0, 0);
                this.spiderLegsModel.renderToBuffer(matrices, v, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }

            renderFleshOverlay(e, this.getParentModel(), this.getTextureLocation(entity), matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
        }
    }

    public static <T extends Entity> void renderFleshOverlay(PossibleDisguisedThing e, EntityModel<T> model, ResourceLocation texture, PoseStack matrices, MultiBufferSource vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch){
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
            matrices.pushPose();
            model.prepareMobModel(entity, limbAngle, limbDistance, tickDelta);
            model.setupAnim(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(ThingOverlayTexture.getFleshOverlayRenderLayer(texture));
            model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, progress);
            matrices.popPose();
        }
    }
}
