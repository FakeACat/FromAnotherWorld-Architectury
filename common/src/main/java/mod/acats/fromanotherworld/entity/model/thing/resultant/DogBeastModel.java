package mod.acats.fromanotherworld.entity.model.thing.resultant;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.entity.thing.resultant.DogBeast;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class DogBeastModel extends GeoModel<DogBeast> {
    @Override
    public ResourceLocation getModelResource(DogBeast object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/dogbeast.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DogBeast object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/dogbeast.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DogBeast animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/dogbeast.animation.json");
    }

    @Override
    public RenderType getRenderType(DogBeast animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
    }
}
