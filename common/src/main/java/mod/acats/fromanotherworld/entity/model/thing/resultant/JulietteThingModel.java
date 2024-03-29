package mod.acats.fromanotherworld.entity.model.thing.resultant;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.entity.thing.resultant.JulietteThing;
import mod.acats.fromanotherworld.constants.VariantID;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class JulietteThingModel extends GeoModel<JulietteThing> {
    @Override
    public ResourceLocation getModelResource(JulietteThing object) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "geo/entity/thing/resultant/juliette_thing.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(JulietteThing object) {
        String variant = "juliette_thing";
        if (object.getVariantID() == VariantID.VILLAGER)
            variant = "juliette_thing_villagertrousers";
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/entity/thing/resultant/juliette_thing/" + variant + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(JulietteThing animatable) {
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "animations/entity/thing/resultant/juliette_thing.animation.json");
    }

    @Override
    public RenderType getRenderType(JulietteThing animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
    }
}
