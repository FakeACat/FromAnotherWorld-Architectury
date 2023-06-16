package acats.fromanotherworld.entity.render.thing.revealed;

import acats.fromanotherworld.entity.model.thing.revealed.ChestSpitterModel;
import acats.fromanotherworld.entity.render.thing.ThingRenderer;
import acats.fromanotherworld.entity.thing.revealed.ChestSpitter;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ChestSpitterRenderer extends ThingRenderer<ChestSpitter> {
    public ChestSpitterRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ChestSpitterModel());
        this.shadowRadius = 0.0F;
    }
}
