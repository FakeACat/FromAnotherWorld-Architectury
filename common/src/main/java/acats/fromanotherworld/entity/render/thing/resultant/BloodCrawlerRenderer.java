package acats.fromanotherworld.entity.render.thing.resultant;

import acats.fromanotherworld.entity.model.thing.resultant.BloodCrawlerModel;
import acats.fromanotherworld.entity.render.thing.ThingRenderer;
import acats.fromanotherworld.entity.thing.resultant.BloodCrawler;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BloodCrawlerRenderer extends ThingRenderer<BloodCrawler> {
    public BloodCrawlerRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new BloodCrawlerModel());
        this.shadowRadius = 0.3F;
    }
}
