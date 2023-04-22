package acats.fromanotherworld.entity.model.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.resultant.BloodCrawlerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class BloodCrawlerEntityModel extends GeoModel<BloodCrawlerEntity> {

    public static String getVariant(BloodCrawlerEntity object){
        return switch (object.getVariant()) {
            case 1 -> "blood_crawler_worm";
            default -> "blood_crawler";
        };
    }

    @Override
    public Identifier getModelResource(BloodCrawlerEntity object) {
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/resultant/" + getVariant(object) + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(BloodCrawlerEntity object) {
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/block/thing_gore.png");
    }

    @Override
    public Identifier getAnimationResource(BloodCrawlerEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/resultant/" + getVariant(animatable) + ".animation.json");
    }
}
