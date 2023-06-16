package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.render.thing.growths.Tentacle;
import acats.fromanotherworld.entity.render.thing.ThingRenderer;
import acats.fromanotherworld.entity.thing.resultant.AbsorberThing;
import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AbsorberThingRenderer<T extends AbsorberThing> extends ThingRenderer<T> {
    public AbsorberThingRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        for (Tentacle tentacle:
             entity.absorbTentacles) {
            tentacle.render(poseStack, bufferSource, partialTick, packedLight);
        }
    }

    @Override
    public boolean shouldRender(T entity, Frustum frustum, double x, double y, double z) {
        for (Tentacle tentacle:
                entity.absorbTentacles) {
            if (tentacle.shouldRender(frustum))
                return true;
        }
        return super.shouldRender(entity, frustum, x, y, z);
    }
}
