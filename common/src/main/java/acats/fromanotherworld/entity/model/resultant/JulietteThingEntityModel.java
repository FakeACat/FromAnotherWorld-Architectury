package acats.fromanotherworld.entity.model.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.AbstractThingEntity;
import acats.fromanotherworld.entity.resultant.JulietteThingEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class JulietteThingEntityModel extends GeoModel<JulietteThingEntity> {
    @Override
    public Identifier getModelResource(JulietteThingEntity object) {
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/resultant/juliette_thing.geo.json");
    }

    @Override
    public Identifier getTextureResource(JulietteThingEntity object) {
        String variant = "juliette_thing";
        if (object.getVictimType() == AbstractThingEntity.VILLAGER)
            variant = "juliette_thing_villagertrousers";
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/resultant/juliette_thing/" + variant + ".png");
    }

    @Override
    public Identifier getAnimationResource(JulietteThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/resultant/juliette_thing.animation.json");
    }

    @Override
    public RenderLayer getRenderType(JulietteThingEntity animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(this.getTextureResource(animatable));
    }
}
