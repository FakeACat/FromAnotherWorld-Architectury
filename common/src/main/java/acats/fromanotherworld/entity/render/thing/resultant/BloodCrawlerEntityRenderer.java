package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.BloodCrawlerEntityModel;
import acats.fromanotherworld.entity.render.thing.ThingEntityRenderer;
import acats.fromanotherworld.entity.thing.resultant.BloodCrawlerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class BloodCrawlerEntityRenderer extends ThingEntityRenderer<BloodCrawlerEntity> {
    public BloodCrawlerEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BloodCrawlerEntityModel());
        this.shadowRadius = 0.3F;
    }
}
