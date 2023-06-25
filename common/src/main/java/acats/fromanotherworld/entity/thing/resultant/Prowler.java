package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.constants.FAWAnimations;
import acats.fromanotherworld.entity.goal.*;
import acats.fromanotherworld.entity.thing.AbsorberThing;
import acats.fromanotherworld.registry.EntityRegistry;
import mod.azure.azurelib.core.animation.AnimatableManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Prowler extends AbsorberThing {

    public Prowler(EntityType<? extends AbsorberThing> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.addThingTargets(false);
        this.goalSelector.addGoal(0, new FleeOnFireGoal(this, 16.0F, 1.2, 1.5));
        this.goalSelector.addGoal(1, new SpitJetGoal(this, 120, 40, 30, 20, new Vec3(1.1D, -0.3D, 0.05D)));
        this.goalSelector.addGoal(2, new AbsorbGoal(this, STANDARD, 1600));
        this.goalSelector.addGoal(3, new ThingAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    public static AttributeSupplier.Builder createProwlerAttributes(){
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.MAX_HEALTH, 75.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public void grow(LivingEntity otherParent) {
        this.growInto(EntityRegistry.BEAST.get());
    }

    @Override
    public Strength getFormStrength() {
        return Strength.STRONG;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(FAWAnimations.defaultThingNoChase(this));
        controllers.add(FAWAnimations.alwaysPlaying(this));
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
}
