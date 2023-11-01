package mod.acats.fromanotherworld.transformation.transformations;

import mod.acats.fromanotherworld.constants.VariantID;
import mod.acats.fromanotherworld.entity.thing.ResizeableThing;
import mod.acats.fromanotherworld.tags.EntityTags;
import mod.acats.fromanotherworld.transformation.TransformationContext;

public interface QuadrupedTransformation<T extends ResizeableThing> extends ResizeableThingTransformation<T> {
    @Override
    default void modifyNewForm(TransformationContext ctx, T newForm) {
        if (ctx.previousType().is(EntityTags.COWS))
            newForm.setVariantID(VariantID.COW);
        else if (ctx.previousType().is(EntityTags.SHEEP))
            newForm.setVariantID(VariantID.SHEEP);
        else if (ctx.previousType().is(EntityTags.PIGS))
            newForm.setVariantID(VariantID.PIG);
        else if (ctx.previousType().is(EntityTags.HORSES))
            newForm.setVariantID(VariantID.HORSE);
        else if (ctx.previousType().is(EntityTags.LLAMAS))
            newForm.setVariantID(VariantID.LLAMA);
        ResizeableThingTransformation.super.modifyNewForm(ctx, newForm);
    }
}
