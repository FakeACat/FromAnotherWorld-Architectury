package mod.acats.fromanotherworld.block.entity.render;

import mod.acats.fromanotherworld.block.entity.AssimilatedSculkOvergrowthBlockEntity;
import mod.acats.fromanotherworld.block.entity.model.AssimilatedSculkOvergrowthBlockEntityModel;
import mod.acats.fromanotherworld.block.interfaces.AssimilatedSculk;
import mod.azure.azurelib.renderer.GeoBlockRenderer;
import net.minecraft.world.phys.Vec3;

public class AssimilatedSculkOvergrowthBlockEntityRenderer extends GeoBlockRenderer<AssimilatedSculkOvergrowthBlockEntity> {
    public AssimilatedSculkOvergrowthBlockEntityRenderer() {
        super(new AssimilatedSculkOvergrowthBlockEntityModel());
    }

    @Override
    public boolean shouldRender(AssimilatedSculkOvergrowthBlockEntity blockEntity, Vec3 vec3) {
        return blockEntity.getBlockState().getValue(AssimilatedSculk.REVEALED) && super.shouldRender(blockEntity, vec3);
    }
}
