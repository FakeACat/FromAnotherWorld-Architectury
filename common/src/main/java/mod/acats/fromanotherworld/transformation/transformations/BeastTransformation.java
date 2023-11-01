package mod.acats.fromanotherworld.transformation.transformations;

import mod.acats.fromanotherworld.entity.thing.resultant.Beast;
import mod.acats.fromanotherworld.registry.EntityRegistry;
import mod.acats.fromanotherworld.tags.EntityTags;
import mod.acats.fromanotherworld.transformation.Transformation;
import mod.acats.fromanotherworld.transformation.TransformationContext;
import net.minecraft.world.entity.EntityType;

public class BeastTransformation implements Transformation<Beast> {
    @Override
    public EntityType<Beast> newForm(TransformationContext ctx) {
        return EntityRegistry.BEAST.get();
    }

    @Override
    public void modifyNewForm(TransformationContext ctx, Beast newForm) {
        newForm.setTier(0, true);
    }

    @Override
    public double weight(TransformationContext ctx) {
        return ctx.previousType().is(EntityTags.VERY_LARGE_QUADRUPEDS) ? STANDARD_WEIGHT : 0.0D;
    }

    @Override
    public double priority(TransformationContext ctx) {
        return 0.0D;
    }
}
