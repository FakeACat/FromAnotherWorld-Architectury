package mod.acats.fromanotherworld.entity.render.thing.resultant;

import mod.acats.fromanotherworld.entity.model.thing.resultant.ProwlerModel;
import mod.acats.fromanotherworld.entity.render.thing.AbsorberThingRenderer;
import mod.acats.fromanotherworld.entity.thing.resultant.Prowler;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ProwlerRenderer extends AbsorberThingRenderer<Prowler> {
    public ProwlerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ProwlerModel());
        this.shadowRadius = 0.6F;
    }
}
