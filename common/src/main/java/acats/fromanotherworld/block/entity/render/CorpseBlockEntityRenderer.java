package acats.fromanotherworld.block.entity.render;

import acats.fromanotherworld.block.entity.CorpseBlockEntity;
import acats.fromanotherworld.block.entity.model.CorpseBlockEntityModel;
import mod.azure.azurelib.renderer.GeoBlockRenderer;

public class CorpseBlockEntityRenderer extends GeoBlockRenderer<CorpseBlockEntity> {
    public CorpseBlockEntityRenderer() {
        super(new CorpseBlockEntityModel());
    }
}
