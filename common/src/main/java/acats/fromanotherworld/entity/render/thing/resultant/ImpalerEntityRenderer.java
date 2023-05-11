package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.ImpalerEntityModel;
import acats.fromanotherworld.entity.render.thing.AbstractThingEntityRenderer;
import acats.fromanotherworld.entity.thing.resultant.ImpalerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class ImpalerEntityRenderer extends AbstractThingEntityRenderer<ImpalerEntity> {
    public ImpalerEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ImpalerEntityModel());
        this.shadowRadius = 0.5F;
    }
}
