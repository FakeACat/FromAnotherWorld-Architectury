package acats.fromanotherworld.entity.thing.resultant;

import acats.fromanotherworld.entity.goal.AbsorbGoal;
import acats.fromanotherworld.entity.goal.FleeOnFireGoal;
import acats.fromanotherworld.entity.goal.ThingAttackGoal;
import acats.fromanotherworld.registry.EntityRegistry;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import static acats.fromanotherworld.constants.Variants.JULIETTE;

public class JulietteThingEntity extends AbsorberThingEntity {

    public JulietteThingEntity(EntityType<? extends JulietteThingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.addThingTargets(false);
        this.goalSelector.addGoal(0, new FleeOnFireGoal(this, 16.0F, 1.2, 1.5));
        this.goalSelector.addGoal(1, new AbsorbGoal(this, STANDARD));
        this.goalSelector.addGoal(2, new ThingAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    public static AttributeSupplier.Builder createJulietteThingAttributes(){
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.ATTACK_DAMAGE, 7.0D).add(Attributes.MAX_HEALTH, 40.0D);
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if (this.isThingFrozen())
            return PlayState.STOP;
        if (event.isMoving()){
            if (this.isAggressive()){
                event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.juliette_thing.chase"));
            }
            else{
                event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.juliette_thing.walk"));
            }
        }
        else{
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.juliette_thing.idle"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void die(DamageSource source) {
        if (random.nextInt(3) == 0){
            CrawlerEntity crawlerEntity = EntityRegistry.CRAWLER.get().create(this.level());
            if (crawlerEntity != null) {
                crawlerEntity.setPos(this.position());
                crawlerEntity.initializeFrom(this);
                crawlerEntity.setVictimType(JULIETTE);
                this.level().addFreshEntity(crawlerEntity);
            }
        }
        super.die(source);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public Strength getFormStrength() {
        return Strength.STANDARD;
    }

    @Override
    public void grow(LivingEntity otherParent) {
        this.growInto(EntityRegistry.SPLIT_FACE.get());
    }
}
