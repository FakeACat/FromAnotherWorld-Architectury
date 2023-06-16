package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.DogBeastSpitterModel;
import acats.fromanotherworld.entity.thing.resultant.DogBeastSpitter;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DogBeastSpitterRenderer extends AbsorberThingRenderer<DogBeastSpitter> {
    public DogBeastSpitterRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new DogBeastSpitterModel());
        this.shadowRadius = 0.5F;
    }
}
