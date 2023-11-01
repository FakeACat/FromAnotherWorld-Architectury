package mod.acats.fromanotherworld.transformation.transformations;

import mod.acats.fromanotherworld.constants.VariantID;
import mod.acats.fromanotherworld.entity.thing.Thing;
import mod.acats.fromanotherworld.tags.EntityTags;
import mod.acats.fromanotherworld.transformation.Transformation;
import mod.acats.fromanotherworld.transformation.TransformationContext;

public interface HumanoidTransformation<T extends Thing> extends Transformation<T> {
    @Override
    default void modifyNewForm(TransformationContext ctx, T newForm) {
        if (ctx.previousType().is(EntityTags.VILLAGERS))
            newForm.setVariantID(VariantID.VILLAGER);
        else if (ctx.previousType().is(EntityTags.ILLAGERS))
            newForm.setVariantID(VariantID.ILLAGER);
    }
}
