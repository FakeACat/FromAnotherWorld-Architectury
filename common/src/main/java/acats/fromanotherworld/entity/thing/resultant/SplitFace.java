package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.config.Config;
import acats.fromanotherworld.constants.FAWAnimations;
import acats.fromanotherworld.entity.goal.*;
import acats.fromanotherworld.entity.interfaces.StalkerThing;
import acats.fromanotherworld.entity.thing.AbsorberThing;
import acats.fromanotherworld.registry.EntityRegistry;
import mod.azure.azurelib.core.animation.AnimatableManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SplitFace extends AbsorberThing implements StalkerThing {
    public SplitFace(EntityType<? extends SplitFace> entityType, Level world) {
        super(entityType, world);
        this.canGrief = true;
    }

    @Override
    protected void registerGoals() {
        this.addThingTargets(false);
        this.goalSelector.addGoal(0, new FleeOnFireGoal(this, 16.0F, 1.2, 1.5));
        this.goalSelector.addGoal(1, new AbsorbGoal(this, STANDARD, Config.DIFFICULTY_CONFIG.splitFaceMergeChance.get()));
        this.goalSelector.addGoal(2, new LeapAttackGoal(this, 2.0D, false, 200, 3.0D, 0.3D, 12.0D));
        this.goalSelector.addGoal(3, new MoveThroughVillageGoal(this, 1.0, false, 4, () -> true));
        this.goalSelector.addGoal(4, new StalkGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    public static AttributeSupplier.Builder createSplitFaceAttributes(){
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public float maxUpStep() {
        return 2.0F;
    }

    @Override
    public boolean rotateWhenClimbing() {
        return true;
    }

    @Override
    public float offsetWhenClimbing() {
        return -0.5F;
    }

    @Override
    public ThingCategory getThingCategory() {
        return ThingCategory.MERGED;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(FAWAnimations.defaultThing(this));
    }

    @Override
    public void grow(LivingEntity otherParent) {
        this.growInto(EntityRegistry.BLAIR_THING.get());
    }

    private Player stalkTarget;

    @Override
    public Player getStalkTarget() {
        return this.stalkTarget;
    }

    @Override
    public void setStalkTarget(Player stalkTarget) {
        this.stalkTarget = stalkTarget;
    }
}
