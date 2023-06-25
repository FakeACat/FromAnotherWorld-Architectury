package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.ProwlerModel;
import acats.fromanotherworld.entity.render.thing.AbsorberThingRenderer;
import acats.fromanotherworld.entity.thing.resultant.Prowler;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ProwlerRenderer extends AbsorberThingRenderer<Prowler> {
    public ProwlerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ProwlerModel());
        this.shadowRadius = 0.6F;
    }
}
