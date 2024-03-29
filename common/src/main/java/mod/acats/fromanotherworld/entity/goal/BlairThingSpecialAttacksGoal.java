package mod.acats.fromanotherworld.entity.goal;

import mod.acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import mod.acats.fromanotherworld.entity.thing.resultant.BlairThing;
import mod.acats.fromanotherworld.entity.thing.resultant.BloodCrawler;
import mod.acats.fromanotherworld.entity.thing.resultant.Crawler;
import mod.acats.fromanotherworld.registry.EntityRegistry;
import mod.acats.fromanotherworld.constants.VariantID;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class BlairThingSpecialAttacksGoal extends Goal {

    BlairThing mob;

    public BlairThingSpecialAttacksGoal(BlairThing mob){
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return this.mob.getMoveCooldown() > BlairThing.TIME_UNTIL_ATTACK_IN_TICKS && this.mob.getMoveCooldown() < BlairThing.MOVE_COOLDOWN_IN_TICKS - (BlairThing.EMERGE_TIME_IN_TICKS + BlairThing.TIME_UNDERGROUND_IN_TICKS + BlairThing.RETREAT_TIME_IN_TICKS) && this.mob.getTarget() != null;
    }

    @Override
    public void start() {
        this.mob.rerollAttack();
    }

    @Override
    public void tick() {
        if (!this.mob.level().isClientSide() && this.mob.getTarget() != null){
            switch (this.mob.getAttack()){
                case 0 -> this.spit();
                case 1 -> this.littleRats();
                case 2 -> this.skipAttack();
            }
        }
    }

    private void spit(){
        if (this.mob.getRandom().nextInt(2) == 0){
            AssimilationLiquidEntity assimilationLiquid = new AssimilationLiquidEntity(this.mob.level(), this.mob.getX(), this.mob.getY(0.55D), this.mob.getZ());
            assimilationLiquid.setOwner(this.mob);
            assimilationLiquid.setDeltaMovement(this.mob.getTarget().position().add(0, this.mob.getTarget().getBbHeight() / 2, 0).subtract(assimilationLiquid.position()).normalize().scale(2.0D).add(new Vec3(this.mob.getRandom().nextInt(40) - 20, this.mob.getRandom().nextInt(40) - 20, this.mob.getRandom().nextInt(40) - 20).scale(0.01f)));
            this.mob.level().addFreshEntity(assimilationLiquid);
        }
    }

    private void littleRats(){
        if (this.mob.getRandom().nextInt(30) == 0){
            BloodCrawler bloodCrawler = EntityRegistry.BLOOD_CRAWLER.get().create(this.mob.level());
            if (bloodCrawler != null) {
                bloodCrawler.moveTo(this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getYRot(), this.mob.getXRot());
                bloodCrawler.initializeFrom(this.mob);
                this.mob.level().addFreshEntity(bloodCrawler);
            }
        }
        if (this.mob.getRandom().nextInt(120) == 0){
            Crawler crawlerEntity = EntityRegistry.CRAWLER.get().create(this.mob.level());
            if (crawlerEntity != null) {
                crawlerEntity.blairSpawned = true;
                crawlerEntity.setVariantID(VariantID.JULIETTE);
                crawlerEntity.moveTo(this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getYRot(), this.mob.getXRot());
                crawlerEntity.initializeFrom(this.mob);
                crawlerEntity.setDeltaMovement(this.mob.getTarget().position().add(0, this.mob.getTarget().getBbHeight() / 2, 0).subtract(crawlerEntity.position()).normalize().scale(1.5D).add(new Vec3(this.mob.getRandom().nextInt(40) - 20, this.mob.getRandom().nextInt(40) - 20, this.mob.getRandom().nextInt(40) - 20).scale(0.01f)));
                this.mob.level().addFreshEntity(crawlerEntity);
            }
        }
    }

    private void skipAttack(){
        if (this.mob.getMoveCooldown() < BlairThing.MOVE_COOLDOWN_IN_TICKS - (BlairThing.EMERGE_TIME_IN_TICKS + BlairThing.TIME_UNDERGROUND_IN_TICKS + BlairThing.RETREAT_TIME_IN_TICKS))
            this.mob.setMoveCooldown(BlairThing.MOVE_COOLDOWN_IN_TICKS - (BlairThing.EMERGE_TIME_IN_TICKS + BlairThing.TIME_UNDERGROUND_IN_TICKS + BlairThing.RETREAT_TIME_IN_TICKS));
    }
}
