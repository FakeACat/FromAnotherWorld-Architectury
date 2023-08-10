package mod.acats.fromanotherworld.entity.render.thing.resultant;

import mod.acats.fromanotherworld.entity.model.thing.resultant.DogBeastSpitterModel;
import mod.acats.fromanotherworld.entity.render.thing.ResizeableThingRenderer;
import mod.acats.fromanotherworld.entity.thing.resultant.DogBeastSpitter;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DogBeastSpitterRenderer extends ResizeableThingRenderer<DogBeastSpitter> {
    public DogBeastSpitterRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new DogBeastSpitterModel());
        this.shadowRadius = 0.5F;
    }
}
