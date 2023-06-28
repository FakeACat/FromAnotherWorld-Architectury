package acats.fromanotherworld.block.entity.model;

import acats.fromanotherworld.block.entity.CorpseBlockEntity;
import acats.fromanotherworld.interfaces.VariableGeoModel;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class CorpseBlockEntityModel extends GeoModel<CorpseBlockEntity> implements VariableGeoModel<CorpseBlockEntity> {
    @Override
    public ResourceLocation getModelResource(CorpseBlockEntity animatable) {
        return this.getVariantModelResource(animatable);
    }

    @Override
    public ResourceLocation getTextureResource(CorpseBlockEntity animatable) {
        return this.getVariantTextureResource(animatable);
    }

    @Override
    public ResourceLocation getAnimationResource(CorpseBlockEntity animatable) {
        return this.getVariantAnimationResource(animatable);
    }

    @Override
    public String getVariant(CorpseBlockEntity animatable) {
        return "corpse_" + animatable.getCorpseType().getSerializedName();
    }

    @Override
    public String getPath() {
        return "block/corpse/";
    }
}
