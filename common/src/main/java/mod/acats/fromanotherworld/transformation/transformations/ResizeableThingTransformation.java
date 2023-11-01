package mod.acats.fromanotherworld.transformation.transformations;

import mod.acats.fromanotherworld.entity.thing.ResizeableThing;
import mod.acats.fromanotherworld.transformation.Transformation;
import mod.acats.fromanotherworld.transformation.TransformationContext;

public interface ResizeableThingTransformation<T extends ResizeableThing> extends Transformation<T> {

    @Override
    default void modifyNewForm(TransformationContext ctx, T newForm) {
        float scale = scaleMultiplier(ctx, newForm);
        newForm.setupScale(ctx.prevWidth() * scale, ctx.prevHeight() * scale);
    }

    default float scaleMultiplier(TransformationContext ctx, T newForm) {
        return 1.0F;
    }
}
