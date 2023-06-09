package acats.fromanotherworld.entity.render.thing.special;

import acats.fromanotherworld.entity.model.thing.special.AlienThingEntityModel;
import acats.fromanotherworld.entity.render.thing.ThingEntityRenderer;
import acats.fromanotherworld.entity.render.feature.AlienThingOverlayFeatureRenderer;
import acats.fromanotherworld.entity.thing.special.AlienThingEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AlienThingEntityRenderer extends ThingEntityRenderer<AlienThingEntity> {
    public AlienThingEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AlienThingEntityModel());
        this.shadowRadius = 0.4F;
        this.addRenderLayer(new AlienThingOverlayFeatureRenderer<>(this));
    }
}
