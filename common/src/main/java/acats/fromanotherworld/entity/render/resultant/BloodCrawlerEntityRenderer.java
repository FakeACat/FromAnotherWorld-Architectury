package acats.fromanotherworld.entity.render.resultant;

import acats.fromanotherworld.entity.model.resultant.BloodCrawlerEntityModel;
import acats.fromanotherworld.entity.render.AbstractThingEntityRenderer;
import acats.fromanotherworld.entity.resultant.BloodCrawlerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class BloodCrawlerEntityRenderer extends AbstractThingEntityRenderer<BloodCrawlerEntity> {
    public BloodCrawlerEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BloodCrawlerEntityModel());
        this.shadowRadius = 0.3F;
    }
}
