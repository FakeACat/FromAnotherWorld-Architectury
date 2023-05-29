package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.DogBeastEntityModel;
import acats.fromanotherworld.entity.thing.resultant.DogBeastEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class DogBeastEntityRenderer extends AbsorberThingEntityRenderer<DogBeastEntity> {

    public DogBeastEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DogBeastEntityModel());
        this.shadowRadius = 0.5F;
    }
}
