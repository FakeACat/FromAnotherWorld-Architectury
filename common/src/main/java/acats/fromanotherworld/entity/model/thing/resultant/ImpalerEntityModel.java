package acats.fromanotherworld.entity.model.thing.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.interfaces.VariableThingModel;
import acats.fromanotherworld.entity.thing.resultant.ImpalerEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class ImpalerEntityModel extends GeoModel<ImpalerEntity> implements VariableThingModel<ImpalerEntity> {
    @Override
    public Identifier getModelResource(ImpalerEntity animatable) {
        return this.getVariantModelResource(animatable);
    }

    @Override
    public Identifier getTextureResource(ImpalerEntity animatable) {
        String texture = this.getVariant(animatable);
        if (!animatable.hasBackNeedles() && !animatable.hasMouthNeedles())
            texture = texture + "_no_needles";
        else if (!animatable.hasBackNeedles())
            texture = texture + "_no_back_needles";
        else if (!animatable.hasMouthNeedles())
            texture = texture + "_no_mouth_needles";
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/" + this.getPath() + texture + ".png");
    }

    @Override
    public Identifier getAnimationResource(ImpalerEntity animatable) {
        return this.getVariantAnimationResource(animatable);
    }

    @Override
    public RenderLayer getRenderType(ImpalerEntity animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(this.getTextureResource(animatable));
    }

    @Override
    public String getVariant(ImpalerEntity animatable) {
        return "impaler";
    }

    @Override
    public String getPath() {
        return "entity/thing/resultant/impaler/";
    }
}
