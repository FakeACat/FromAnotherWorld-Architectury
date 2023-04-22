package acats.fromanotherworld.entity.resultant;

import acats.fromanotherworld.entity.AbstractThingEntity;
import acats.fromanotherworld.entity.goal.FleeOnFireGoal;
import acats.fromanotherworld.entity.goal.MergeGoal;
import acats.fromanotherworld.entity.goal.ThingAttackGoal;
import acats.fromanotherworld.registry.EntityRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class JulietteThingEntity extends AbstractThingEntity {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public JulietteThingEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.addThingTargets(false);
        this.goalSelector.add(0, new FleeOnFireGoal(this, 16.0F, 1.2, 1.5));
        this.goalSelector.add(1, new ThingAttackGoal(this, 1.0D, false));
        this.goalSelector.add(2, new MergeGoal(this, EntityRegistry.BLAIR_THING.get()));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
    }

    public static DefaultAttributeContainer.Builder createJulietteThingAttributes(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0D).add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0D);
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
        if (this.isThingFrozen())
            return PlayState.STOP;
        if (event.isMoving()){
            if (this.isAttacking()){
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
    public void onDeath(DamageSource source) {
        if (random.nextInt(3) == 0){
            CrawlerEntity crawlerEntity = EntityRegistry.CRAWLER.get().create(this.world);
            if (crawlerEntity != null) {
                crawlerEntity.setPosition(this.getPos());
                crawlerEntity.setVictimType("fromanotherworld:juliette_thing");
                this.world.spawnEntity(crawlerEntity);
            }
        }
        super.onDeath(source);
    }

    @Override
    public boolean canMerge() {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public Strength getFormStrength() {
        return Strength.STANDARD;
    }
}
