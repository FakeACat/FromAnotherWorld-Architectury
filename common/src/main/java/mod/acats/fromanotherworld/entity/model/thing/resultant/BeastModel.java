package mod.acats.fromanotherworld.entity.model.thing.resultant;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.entity.thing.resultant.Beast;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class BeastModel extends GeoModel<Beast> {
    @Override
    public ResourceLocation getModelResource(Beast animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/beast.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Beast animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/beast.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Beast animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/beast.animation.json");
    }
}
