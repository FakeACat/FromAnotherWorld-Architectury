package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.resultant.JulietteThingEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import static acats.fromanotherworld.constants.Variants.VILLAGER;

public class JulietteThingEntityModel extends GeoModel<JulietteThingEntity> {
    @Override
    public ResourceLocation getModelResource(JulietteThingEntity object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/juliette_thing.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(JulietteThingEntity object) {
        String variant = "juliette_thing";
        if (object.getVictimType() == VILLAGER)
            variant = "juliette_thing_villagertrousers";
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/juliette_thing/" + variant + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(JulietteThingEntity animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/juliette_thing.animation.json");
    }

    @Override
    public RenderType getRenderType(JulietteThingEntity animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
    }
}
