package mod.acats.fromanotherworld.transformation.transformations;

import mod.acats.fromanotherworld.entity.thing.resultant.JulietteThing;
import mod.acats.fromanotherworld.registry.EntityRegistry;
import mod.acats.fromanotherworld.tags.EntityTags;
import mod.acats.fromanotherworld.transformation.TransformationContext;
import net.minecraft.world.entity.EntityType;

public class JulietteThingTransformation implements HumanoidTransformation<JulietteThing> {
    @Override
    public EntityType<JulietteThing> newForm(TransformationContext ctx) {
        return EntityRegistry.JULIETTE_THING.get();
    }

    @Override
    public double weight(TransformationContext ctx) {
        return ctx.previousType().is(EntityTags.HUMANOIDS) ? STANDARD_WEIGHT : 0.0D;
    }

    @Override
    public double priority(TransformationContext ctx) {
        return 0.0D;
    }
}
