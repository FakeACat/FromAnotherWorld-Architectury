package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.ImpalerEntityModel;
import acats.fromanotherworld.entity.render.thing.ThingEntityRenderer;
import acats.fromanotherworld.entity.thing.resultant.ImpalerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class ImpalerEntityRenderer extends ThingEntityRenderer<ImpalerEntity> {
    public ImpalerEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ImpalerEntityModel());
        this.shadowRadius = 0.5F;
    }
}
