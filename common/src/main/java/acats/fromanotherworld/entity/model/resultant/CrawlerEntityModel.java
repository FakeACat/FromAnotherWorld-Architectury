package acats.fromanotherworld.entity.model.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.resultant.CrawlerEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class CrawlerEntityModel extends GeoModel<CrawlerEntity> {
    @Override
    public Identifier getModelResource(CrawlerEntity object) {
        String variant = "crawler";
        if (object.wasVillager() || object.wasIllager())
            variant = "crawler_villager";
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/resultant/crawler/" + variant + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(CrawlerEntity object) {
        String variant = "crawler";
        if (object.wasVillager())
            variant = "crawler_villager";
        if (object.wasIllager())
            variant = "crawler_illager";
        if (Objects.equals(object.getVictimType(), "fromanotherworld:juliette_thing"))
            variant = "crawler_juliette_thing";
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/resultant/crawler/" + variant + ".png");
    }

    @Override
    public Identifier getAnimationResource(CrawlerEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/resultant/crawler.animation.json");
    }
}
