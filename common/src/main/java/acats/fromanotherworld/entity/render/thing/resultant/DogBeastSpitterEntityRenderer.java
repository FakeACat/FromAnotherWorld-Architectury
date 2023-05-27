package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.DogBeastSpitterEntityModel;
import acats.fromanotherworld.entity.render.thing.ThingEntityRenderer;
import acats.fromanotherworld.entity.thing.resultant.DogBeastSpitterEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class DogBeastSpitterEntityRenderer extends ThingEntityRenderer<DogBeastSpitterEntity> {
    public DogBeastSpitterEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DogBeastSpitterEntityModel());
        this.shadowRadius = 0.5F;
    }
}
