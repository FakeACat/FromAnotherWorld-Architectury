package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.BloodCrawlerEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class BloodCrawlerEntityModel extends GeoModel<BloodCrawlerEntity> {

    public static String getVariant(BloodCrawlerEntity object){
        return switch (object.getVariant()) {
            case 1 -> "blood_crawler_worm";
            default -> "blood_crawler";
        };
    }

    @Override
    public ResourceLocation getModelResource(BloodCrawlerEntity object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/" + getVariant(object) + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BloodCrawlerEntity object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/block/thing_gore.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BloodCrawlerEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/" + getVariant(animatable) + ".animation.json");
    }
}
