package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.interfaces.VariableThing;
import acats.fromanotherworld.entity.thing.resultant.CrawlerEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class CrawlerEntityModel extends GeoModel<CrawlerEntity> implements VariableThing {
    @Override
    public Identifier getModelResource(CrawlerEntity object) {
        String variant = "crawler";
        if (object.getVictimType() == VILLAGER ||
                object.getVictimType() == ILLAGER)
            variant = "crawler_villager";
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/crawler/" + variant + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(CrawlerEntity object) {
        String variant = "crawler";
        if (object.getVictimType() == VILLAGER)
            variant = "crawler_villager";
        else if (object.getVictimType() == ILLAGER)
            variant = "crawler_illager";
        else if (object.getVictimType() == JULIETTE)
            variant = "crawler_juliette_thing";
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/crawler/" + variant + ".png");
    }

    @Override
    public Identifier getAnimationResource(CrawlerEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/crawler.animation.json");
    }
}
