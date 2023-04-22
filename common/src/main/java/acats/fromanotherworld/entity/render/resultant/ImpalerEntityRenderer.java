package acats.fromanotherworld.entity.render.resultant;

import acats.fromanotherworld.entity.model.resultant.ImpalerEntityModel;
import acats.fromanotherworld.entity.render.AbstractThingEntityRenderer;
import acats.fromanotherworld.entity.resultant.ImpalerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class ImpalerEntityRenderer extends AbstractThingEntityRenderer<ImpalerEntity> {
    public ImpalerEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ImpalerEntityModel());
        this.shadowRadius = 0.5F;
    }
}
