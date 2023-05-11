package acats.fromanotherworld.entity.render.thing.special;

import acats.fromanotherworld.entity.model.thing.special.AlienThingEntityModel;
import acats.fromanotherworld.entity.render.thing.AbstractThingEntityRenderer;
import acats.fromanotherworld.entity.render.feature.AlienThingOverlayFeatureRenderer;
import acats.fromanotherworld.entity.thing.special.AlienThingEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class AlienThingEntityRenderer extends AbstractThingEntityRenderer<AlienThingEntity> {
    public AlienThingEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AlienThingEntityModel());
        this.shadowRadius = 0.4F;
        this.addRenderLayer(new AlienThingOverlayFeatureRenderer<>(this));
    }
}
