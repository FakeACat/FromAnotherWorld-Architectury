package acats.fromanotherworld.entity.model.misc;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.misc.StarshipEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class StarshipEntityModel extends GeoModel<StarshipEntity> {
    @Override
    public Identifier getModelResource(StarshipEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/misc/starship.geo.json");
    }

    @Override
    public Identifier getTextureResource(StarshipEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/misc/starship.png");
    }

    @Override
    public Identifier getAnimationResource(StarshipEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/misc/starship.animation.json");
    }
}
