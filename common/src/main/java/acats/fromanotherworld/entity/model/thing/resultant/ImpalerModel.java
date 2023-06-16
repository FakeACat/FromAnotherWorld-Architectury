package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.interfaces.VariableThingModel;
import acats.fromanotherworld.entity.thing.resultant.Impaler;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ImpalerModel extends GeoModel<Impaler> implements VariableThingModel<Impaler> {
    @Override
    public ResourceLocation getModelResource(Impaler animatable) {
        return this.getVariantModelResource(animatable);
    }

    @Override
    public ResourceLocation getTextureResource(Impaler animatable) {
        String texture = this.getVariant(animatable);
        if (!animatable.hasBackNeedles() && !animatable.hasMouthNeedles())
            texture = texture + "_no_needles";
        else if (!animatable.hasBackNeedles())
            texture = texture + "_no_back_needles";
        else if (!animatable.hasMouthNeedles())
            texture = texture + "_no_mouth_needles";
        return new ResourceLocation(FromAnotherWorld.MOD_ID, "textures/" + this.getPath() + texture + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(Impaler animatable) {
        return this.getVariantAnimationResource(animatable);
    }

    @Override
    public RenderType getRenderType(Impaler animatable, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
    }

    @Override
    public String getVariant(Impaler animatable) {
        return "impaler";
    }

    @Override
    public String getPath() {
        return "entity/thing/resultant/impaler/";
    }
}
