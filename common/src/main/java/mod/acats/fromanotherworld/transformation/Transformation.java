package mod.acats.fromanotherworld.transformation;

import mod.acats.fromanotherworld.entity.thing.Thing;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;

public interface Transformation<T extends Thing> {
    double SPLIT_WEIGHT = 1.0D;
    double STANDARD_WEIGHT = 1.0D;
    double ELITE_WEIGHT = 0.2D;

    default void transform(TransformationContext ctx) {
        T newForm = newForm(ctx).create(ctx.level());
        if (newForm != null) {
            this.modifyNewForm(ctx, newForm);
            newForm.setCustomName(ctx.name());
            newForm.moveTo(ctx.position().x(), ctx.position().y(), ctx.position().z(), ctx.yRot(), ctx.xRot());
            newForm.finalizeSpawn(ctx.level(), ctx.level().getCurrentDifficultyAt(BlockPos.containing(ctx.position())), MobSpawnType.CONVERSION, null, null);
            newForm.setVictim(ctx.previousFormTag());
            ctx.level().addFreshEntity(newForm);
        }
    }

    /**
     * Used for modifying the new Thing that is about to be created.
     * Potential uses include setting stats based on the previous form, or spawning additional mobs alongside the new form.
     *
     * @param ctx The context of the transformation, containing important information such as the level and position.
     * @param newForm The new Thing that is about to be spawned into the level.
     */
    default void modifyNewForm(TransformationContext ctx, T newForm) {
    }

    /**
     * @param ctx The context of the transformation, containing important information such as the level and position.
     * @return The EntityType of the new form that will be created.
     */
    EntityType<T> newForm(TransformationContext ctx);

    /**
     * This must be deterministic. If repeated calls of this method with the same TransformationContext returns different outputs, problems are likely to occur.
     *
     * @param ctx The context of the transformation, containing important information such as the level and position.
     * @return The relative chance of this transformation being selected amongst other transformations of equal priority.
     */
    double weight(TransformationContext ctx);

    /**
     * Decides the priority of the transformation. Higher priority transformations are ALWAYS selected over lower priority transformations when available.
     * This must be deterministic. If repeated calls of this method with the same TransformationContext returns different outputs, problems are likely to occur.
     *
     * @param ctx The context of the transformation, containing important information such as the level and position.
     * @return The priority of the transformation.
     */
    double priority(TransformationContext ctx);
}
