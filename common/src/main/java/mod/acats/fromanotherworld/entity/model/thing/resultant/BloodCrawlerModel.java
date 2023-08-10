package mod.acats.fromanotherworld.entity.model.thing.resultant;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.entity.thing.resultant.BloodCrawler;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class BloodCrawlerModel extends GeoModel<BloodCrawler> {

    public static String getVariant(BloodCrawler object){
        return switch (object.getVariant()) {
            case 1 -> "blood_crawler_worm";
            default -> "blood_crawler";
        };
    }

    @Override
    public ResourceLocation getModelResource(BloodCrawler object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/" + getVariant(object) + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BloodCrawler object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/block/thing_gore.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BloodCrawler animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/" + getVariant(animatable) + ".animation.json");
    }
}
