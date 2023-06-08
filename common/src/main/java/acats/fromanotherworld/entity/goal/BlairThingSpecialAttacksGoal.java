package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import acats.fromanotherworld.entity.thing.resultant.BlairThingEntity;
import acats.fromanotherworld.entity.thing.resultant.BloodCrawlerEntity;
import acats.fromanotherworld.entity.thing.resultant.CrawlerEntity;
import acats.fromanotherworld.registry.EntityRegistry;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;

import static acats.fromanotherworld.constants.Variants.JULIETTE;
import static acats.fromanotherworld.entity.thing.resultant.BlairThingEntity.*;

public class BlairThingSpecialAttacksGoal extends Goal {

    BlairThingEntity mob;

    public BlairThingSpecialAttacksGoal(BlairThingEntity mob){
        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        return this.mob.getMoveCooldown() > TIME_UNTIL_ATTACK_IN_TICKS && this.mob.getMoveCooldown() < MOVE_COOLDOWN_IN_TICKS - (EMERGE_TIME_IN_TICKS + TIME_UNDERGROUND_IN_TICKS + RETREAT_TIME_IN_TICKS) && this.mob.getTarget() != null;
    }

    @Override
    public void start() {
        this.mob.rerollAttack();
    }

    @Override
    public void tick() {
        if (!this.mob.getWorld().isClient() && this.mob.getTarget() != null){
            switch (this.mob.getAttack()){
                case 0 -> this.spit();
                case 1 -> this.littleRats();
                case 2 -> this.skipAttack();
            }
        }
    }

    private void spit(){
        if (this.mob.getRandom().nextInt(2) == 0){
            AssimilationLiquidEntity assimilationLiquid = new AssimilationLiquidEntity(this.mob.getWorld(), this.mob.getX(), this.mob.getBodyY(0.55D), this.mob.getZ());
            assimilationLiquid.setOwner(this.mob);
            assimilationLiquid.setVelocity(this.mob.getTarget().getPos().add(0, this.mob.getTarget().getHeight() / 2, 0).subtract(assimilationLiquid.getPos()).normalize().multiply(2.0D).add(new Vec3d(this.mob.getRandom().nextInt(40) - 20, this.mob.getRandom().nextInt(40) - 20, this.mob.getRandom().nextInt(40) - 20).multiply(0.01f)));
            this.mob.getWorld().spawnEntity(assimilationLiquid);
        }
    }

    private void littleRats(){
        if (this.mob.getRandom().nextInt(30) == 0){
            BloodCrawlerEntity bloodCrawlerEntity = EntityRegistry.BLOOD_CRAWLER.get().create(this.mob.getWorld());
            if (bloodCrawlerEntity != null) {
                bloodCrawlerEntity.refreshPositionAndAngles(this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getYaw(), this.mob.getPitch());
                bloodCrawlerEntity.initializeFrom(this.mob);
                this.mob.getWorld().spawnEntity(bloodCrawlerEntity);
            }
        }
        if (this.mob.getRandom().nextInt(120) == 0){
            CrawlerEntity crawlerEntity = EntityRegistry.CRAWLER.get().create(this.mob.getWorld());
            if (crawlerEntity != null) {
                crawlerEntity.blairSpawned = true;
                crawlerEntity.setVictimType(JULIETTE);
                crawlerEntity.refreshPositionAndAngles(this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getYaw(), this.mob.getPitch());
                crawlerEntity.initializeFrom(this.mob);
                crawlerEntity.setVelocity(this.mob.getTarget().getPos().add(0, this.mob.getTarget().getHeight() / 2, 0).subtract(crawlerEntity.getPos()).normalize().multiply(1.5D).add(new Vec3d(this.mob.getRandom().nextInt(40) - 20, this.mob.getRandom().nextInt(40) - 20, this.mob.getRandom().nextInt(40) - 20).multiply(0.01f)));
                this.mob.getWorld().spawnEntity(crawlerEntity);
            }
        }
    }

    private void skipAttack(){
        if (this.mob.getMoveCooldown() < MOVE_COOLDOWN_IN_TICKS - (EMERGE_TIME_IN_TICKS + TIME_UNDERGROUND_IN_TICKS + RETREAT_TIME_IN_TICKS))
            this.mob.setMoveCooldown(MOVE_COOLDOWN_IN_TICKS - (EMERGE_TIME_IN_TICKS + TIME_UNDERGROUND_IN_TICKS + RETREAT_TIME_IN_TICKS));
    }
}
