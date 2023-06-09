package acats.fromanotherworld.entity.render.thing.revealed;

import acats.fromanotherworld.entity.model.thing.revealed.ChestSpitterEntityModel;
import acats.fromanotherworld.entity.render.thing.ThingEntityRenderer;
import acats.fromanotherworld.entity.thing.revealed.ChestSpitterEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ChestSpitterEntityRenderer extends ThingEntityRenderer<ChestSpitterEntity> {
    public ChestSpitterEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ChestSpitterEntityModel());
        this.shadowRadius = 0.0F;
    }
}
