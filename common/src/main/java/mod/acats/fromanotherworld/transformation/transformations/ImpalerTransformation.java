package mod.acats.fromanotherworld.transformation.transformations;

import mod.acats.fromanotherworld.entity.thing.resultant.Impaler;
import mod.acats.fromanotherworld.registry.EntityRegistry;
import mod.acats.fromanotherworld.tags.EntityTags;
import mod.acats.fromanotherworld.transformation.TransformationContext;
import net.minecraft.world.entity.EntityType;

public class ImpalerTransformation implements QuadrupedTransformation<Impaler> {
    @Override
    public EntityType<Impaler> newForm(TransformationContext ctx) {
        return EntityRegistry.IMPALER.get();
    }

    @Override
    public double weight(TransformationContext ctx) {
        return ctx.previousType().is(EntityTags.QUADRUPEDS) ? ELITE_WEIGHT : 0.0D;
    }

    @Override
    public double priority(TransformationContext ctx) {
        return 0.0D;
    }
}
