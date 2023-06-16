package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.DogBeastModel;
import acats.fromanotherworld.entity.thing.resultant.DogBeast;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DogBeastRenderer extends AbsorberThingRenderer<DogBeast> {

    public DogBeastRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new DogBeastModel());
        this.shadowRadius = 0.5F;
    }
}
