package mod.acats.fromanotherworld.transformation.transformations;

import mod.acats.fromanotherworld.entity.thing.resultant.BloodCrawler;
import mod.acats.fromanotherworld.entity.thing.resultant.DogBeastSpitter;
import mod.acats.fromanotherworld.registry.EntityRegistry;
import mod.acats.fromanotherworld.tags.EntityTags;
import mod.acats.fromanotherworld.transformation.TransformationContext;
import net.minecraft.world.entity.EntityType;

public class DogBeastSpitterTransformation implements QuadrupedTransformation<DogBeastSpitter> {
    @Override
    public EntityType<DogBeastSpitter> newForm(TransformationContext ctx) {
        return EntityRegistry.DOGBEAST_SPITTER.get();
    }

    @Override
    public void modifyNewForm(TransformationContext ctx, DogBeastSpitter newForm) {
        QuadrupedTransformation.super.modifyNewForm(ctx, newForm);

        for (int i = 0; i < 3; i++){
            BloodCrawler bloodCrawler = EntityRegistry.BLOOD_CRAWLER.get().create(ctx.level());
            if (bloodCrawler != null) {
                bloodCrawler.moveTo(ctx.position().x(), ctx.position().y(), ctx.position().z(), ctx.yRot(), ctx.xRot());
                bloodCrawler.initializeFrom(newForm);
                ctx.level().addFreshEntity(bloodCrawler);
            }
        }
    }

    @Override
    public double weight(TransformationContext ctx) {
        return ctx.previousType().is(EntityTags.QUADRUPEDS) ? SPLIT_WEIGHT : 0.0D;
    }

    @Override
    public double priority(TransformationContext ctx) {
        return 0.0D;
    }

    @Override
    public float scaleMultiplier(TransformationContext ctx, DogBeastSpitter newForm) {
        return 0.7F;
    }
}
