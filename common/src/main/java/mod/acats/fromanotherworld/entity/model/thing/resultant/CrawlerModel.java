package mod.acats.fromanotherworld.entity.model.thing.resultant;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.entity.thing.resultant.Crawler;
import mod.acats.fromanotherworld.constants.VariantID;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class CrawlerModel extends GeoModel<Crawler> {
    @Override
    public ResourceLocation getModelResource(Crawler object) {
        String variant = "crawler";
        if (object.getVariantID() == VariantID.VILLAGER ||
                object.getVariantID() == VariantID.ILLAGER)
            variant = "crawler_villager";
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/crawler/" + variant + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Crawler object) {
        String variant = "crawler";
        if (object.getVariantID() == VariantID.VILLAGER)
            variant = "crawler_villager";
        else if (object.getVariantID() == VariantID.ILLAGER)
            variant = "crawler_illager";
        else if (object.getVariantID() == VariantID.JULIETTE)
            variant = "crawler_juliette_thing";
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/crawler/" + variant + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(Crawler animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/crawler.animation.json");
    }
}
