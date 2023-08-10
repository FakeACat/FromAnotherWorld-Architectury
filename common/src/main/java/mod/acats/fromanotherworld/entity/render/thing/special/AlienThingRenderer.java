package mod.acats.fromanotherworld.entity.render.thing.special;

import mod.acats.fromanotherworld.entity.model.thing.special.AlienThingModel;
import mod.acats.fromanotherworld.entity.render.thing.ThingRenderer;
import mod.acats.fromanotherworld.entity.render.feature.AlienThingOverlayFeatureRenderer;
import mod.acats.fromanotherworld.entity.thing.special.AlienThing;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AlienThingRenderer extends ThingRenderer<AlienThing> {
    public AlienThingRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AlienThingModel());
        this.shadowRadius = 0.4F;
        this.addRenderLayer(new AlienThingOverlayFeatureRenderer<>(this));
    }
}
