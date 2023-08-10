package mod.acats.fromanotherworld.entity.render.thing.resultant;

import mod.acats.fromanotherworld.entity.model.thing.resultant.BloodCrawlerModel;
import mod.acats.fromanotherworld.entity.render.thing.ThingRenderer;
import mod.acats.fromanotherworld.entity.thing.resultant.BloodCrawler;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BloodCrawlerRenderer extends ThingRenderer<BloodCrawler> {
    public BloodCrawlerRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new BloodCrawlerModel());
        this.shadowRadius = 0.3F;
    }
}
