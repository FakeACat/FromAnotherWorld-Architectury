package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.constants.VariantID;
import acats.fromanotherworld.entity.thing.resultant.PalmerThing;
import acats.fromanotherworld.interfaces.VariableGeoModel;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class PalmerThingModel extends GeoModel<PalmerThing> implements VariableGeoModel<PalmerThing> {
    @Override
    public ResourceLocation getModelResource(PalmerThing animatable) {
        return this.getVariantModelResource(animatable);
    }

    @Override
    public ResourceLocation getTextureResource(PalmerThing animatable) {
        return this.getVariantTextureResource(animatable);
    }

    @Override
    public ResourceLocation getAnimationResource(PalmerThing animatable) {
        return this.getVariantAnimationResource(animatable);
    }

    @Override
    public String getVariant(PalmerThing animatable) {
        return animatable.getVariantID() == VariantID.GORE ? "palmer_thing_gore" : "palmer_thing";
    }

    @Override
    public String getPath() {
        return "entity/thing/resultant/palmer_thing/";
    }
}
