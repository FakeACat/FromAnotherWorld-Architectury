package acats.fromanotherworld.entity.model.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.resultant.ImpalerEntity;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class ImpalerEntityModel extends GeoModel<ImpalerEntity> {
    @Override
    public Identifier getModelResource(ImpalerEntity object) {
        return new Identifier(FromAnotherWorld.MOD_ID, "geo/entity/resultant/impaler.geo.json");
    }

    @Override
    public Identifier getTextureResource(ImpalerEntity object) {
        String texture = "impaler";
        if (!object.hasBackNeedles() && !object.hasMouthNeedles())
            texture = "impaler_no_needles";
        else if (!object.hasBackNeedles())
            texture = "impaler_no_back_needles";
        else if (!object.hasMouthNeedles())
            texture = "impaler_no_mouth_needles";
        return new Identifier(FromAnotherWorld.MOD_ID, "textures/entity/resultant/impaler/" + texture + ".png");
    }

    @Override
    public Identifier getAnimationResource(ImpalerEntity animatable) {
        return new Identifier(FromAnotherWorld.MOD_ID, "animations/entity/resultant/impaler.animation.json");
    }

    @Override
    public RenderLayer getRenderType(ImpalerEntity animatable, Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(this.getTextureResource(animatable));
    }
}
