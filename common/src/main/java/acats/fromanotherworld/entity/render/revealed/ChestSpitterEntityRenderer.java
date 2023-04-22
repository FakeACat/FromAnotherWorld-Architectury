package acats.fromanotherworld.entity.render.revealed;

import acats.fromanotherworld.entity.model.revealed.ChestSpitterEntityModel;
import acats.fromanotherworld.entity.render.AbstractThingEntityRenderer;
import acats.fromanotherworld.entity.revealed.ChestSpitterEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class ChestSpitterEntityRenderer extends AbstractThingEntityRenderer<ChestSpitterEntity> {
    public ChestSpitterEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ChestSpitterEntityModel());
        this.shadowRadius = 0.0F;
    }
}
