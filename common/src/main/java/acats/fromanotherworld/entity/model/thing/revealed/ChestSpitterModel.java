package acats.fromanotherworld.entity.model.thing.revealed;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.thing.revealed.ChestSpitter;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ChestSpitterModel extends GeoModel<ChestSpitter> {
    @Override
    public ResourceLocation getModelResource(ChestSpitter object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/revealed/chest_spitter.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ChestSpitter object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/revealed/chest_spitter/chest_spitter.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ChestSpitter animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/revealed/chest_spitter.animation.json");
    }

    @Override
    public RenderType getRenderType(ChestSpitter animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
    }
}
