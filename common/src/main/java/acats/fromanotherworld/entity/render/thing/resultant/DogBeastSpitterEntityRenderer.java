package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.DogBeastSpitterEntityModel;
import acats.fromanotherworld.entity.thing.resultant.DogBeastSpitterEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DogBeastSpitterEntityRenderer extends AbsorberThingEntityRenderer<DogBeastSpitterEntity> {
    public DogBeastSpitterEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new DogBeastSpitterEntityModel());
        this.shadowRadius = 0.5F;
    }
}
