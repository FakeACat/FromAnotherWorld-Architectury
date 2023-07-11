package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.constants.VariantID;
import acats.fromanotherworld.entity.model.thing.resultant.ImpalerModel;
import acats.fromanotherworld.entity.render.thing.ResizeableThingRenderer;
import acats.fromanotherworld.entity.thing.resultant.Impaler;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ImpalerRenderer extends ResizeableThingRenderer<Impaler> {
    public ImpalerRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ImpalerModel());
        this.shadowRadius = 0.5F;
    }

    @Override
    public float originalModelScale(Impaler thing) {
        if (thing.getVariantID() == VariantID.LLAMA)
            return 3.67F;
        return super.originalModelScale(thing);
    }
}
