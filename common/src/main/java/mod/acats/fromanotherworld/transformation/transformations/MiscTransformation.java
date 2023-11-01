package mod.acats.fromanotherworld.transformation.transformations;

import mod.acats.fromanotherworld.entity.thing.resultant.BloodCrawler;
import mod.acats.fromanotherworld.registry.EntityRegistry;
import mod.acats.fromanotherworld.tags.EntityTags;
import mod.acats.fromanotherworld.transformation.Transformation;
import mod.acats.fromanotherworld.transformation.TransformationContext;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;

public class MiscTransformation implements Transformation<BloodCrawler> {
    @Override
    public EntityType<BloodCrawler> newForm(TransformationContext ctx) {
        return EntityRegistry.BLOOD_CRAWLER.get();
    }

    @Override
    public void transform(TransformationContext ctx) {
        float volume = ctx.prevWidth() * ctx.prevWidth() * ctx.prevHeight();

        for (int i = 0; i < volume * 4.0F; i++){
            BloodCrawler bloodCrawler = this.newForm(ctx).create(ctx.level());
            if (bloodCrawler != null) {
                bloodCrawler.moveTo(ctx.position().x(), ctx.position().y(), ctx.position().z(), ctx.yRot(), ctx.xRot());
                bloodCrawler.finalizeSpawn(ctx.level(), ctx.level().getCurrentDifficultyAt(ctx.blockPosition()), MobSpawnType.CONVERSION, null, null);
                ctx.level().addFreshEntity(bloodCrawler);
            }
        }
    }

    @Override
    public double weight(TransformationContext ctx) {
        return ctx.previousType().is(EntityTags.MISC) ? STANDARD_WEIGHT : 0.0D;
    }

    @Override
    public double priority(TransformationContext ctx) {
        return 0.0D;
    }
}
