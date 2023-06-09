package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.Crawler;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

import static acats.fromanotherworld.constants.Variants.*;

public class CrawlerModel extends GeoModel<Crawler> {
    @Override
    public ResourceLocation getModelResource(Crawler object) {
        String variant = "crawler";
        if (object.getVictimType() == VILLAGER ||
                object.getVictimType() == ILLAGER)
            variant = "crawler_villager";
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/crawler/" + variant + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Crawler object) {
        String variant = "crawler";
        if (object.getVictimType() == VILLAGER)
            variant = "crawler_villager";
        else if (object.getVictimType() == ILLAGER)
            variant = "crawler_illager";
        else if (object.getVictimType() == JULIETTE)
            variant = "crawler_juliette_thing";
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/crawler/" + variant + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(Crawler animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/crawler.animation.json");
    }
}
