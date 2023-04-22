package acats.fromanotherworld.entity.render.resultant;

import acats.fromanotherworld.entity.model.resultant.DogBeastEntityModel;
import acats.fromanotherworld.entity.render.AbstractThingEntityRenderer;
import acats.fromanotherworld.entity.resultant.DogBeastEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class DogBeastEntityRenderer extends AbstractThingEntityRenderer<DogBeastEntity> {

    public DogBeastEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DogBeastEntityModel());
        this.shadowRadius = 0.5F;
    }
}
