package acats.fromanotherworld.entity.render.resultant;

import acats.fromanotherworld.entity.model.resultant.DogBeastSpitterEntityModel;
import acats.fromanotherworld.entity.render.AbstractThingEntityRenderer;
import acats.fromanotherworld.entity.resultant.DogBeastSpitterEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class DogBeastSpitterEntityRenderer extends AbstractThingEntityRenderer<DogBeastSpitterEntity> {
    public DogBeastSpitterEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DogBeastSpitterEntityModel());
        this.shadowRadius = 0.5F;
    }
}
