package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.ImpalerModel;
import acats.fromanotherworld.entity.thing.resultant.Impaler;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ImpalerRenderer extends AbsorberThingRenderer<Impaler> {
    public ImpalerRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ImpalerModel());
        this.shadowRadius = 0.5F;
    }
}
