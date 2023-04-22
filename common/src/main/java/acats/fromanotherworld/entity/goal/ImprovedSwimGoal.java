package acats.fromanotherworld.entity.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;

public class ImprovedSwimGoal extends SwimGoal {
    private final MobEntity mob;
    private final float speed;
    public ImprovedSwimGoal(MobEntity mob, float speed) {
        super(mob);
        this.mob = mob;
        this.speed = speed;
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (this.mob.isInLava() || target == null){
            super.tick();
        }
        else{
            this.mob.setVelocity(target.getPos().add(0, target.getHeight() / 2, 0).subtract(this.mob.getPos()).normalize().multiply(mob.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * speed));
            this.mob.getNavigation().stop();
            this.mob.lookAtEntity(target, 10, 10);
        }
    }
}
