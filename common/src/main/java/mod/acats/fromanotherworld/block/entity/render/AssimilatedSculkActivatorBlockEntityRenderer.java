package mod.acats.fromanotherworld.block.entity.render;

import mod.acats.fromanotherworld.block.entity.AssimilatedSculkActivatorBlockEntity;
import mod.acats.fromanotherworld.block.entity.model.AssimilatedSculkActivatorBlockEntityModel;
import mod.acats.fromanotherworld.block.interfaces.AssimilatedSculk;
import mod.azure.azurelib.renderer.GeoBlockRenderer;
import net.minecraft.world.phys.Vec3;

public class AssimilatedSculkActivatorBlockEntityRenderer extends GeoBlockRenderer<AssimilatedSculkActivatorBlockEntity> {
    public AssimilatedSculkActivatorBlockEntityRenderer() {
        super(new AssimilatedSculkActivatorBlockEntityModel());
    }

    @Override
    public boolean shouldRender(AssimilatedSculkActivatorBlockEntity blockEntity, Vec3 vec3) {
        return blockEntity.getBlockState().getValue(AssimilatedSculk.REVEALED) && super.shouldRender(blockEntity, vec3);
    }
}
