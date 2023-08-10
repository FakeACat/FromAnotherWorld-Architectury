package mod.acats.fromanotherworld.entity.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;

public class ImprovedSwimGoal extends FloatGoal {
    private final Mob mob;
    private final float speed;
    public ImprovedSwimGoal(Mob mob, float speed) {
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
            this.mob.setDeltaMovement(target.position().add(0, target.getBbHeight() / 2, 0).subtract(this.mob.position()).normalize().scale(mob.getAttributeValue(Attributes.MOVEMENT_SPEED) * speed));
            this.mob.getNavigation().stop();
            this.mob.lookAt(target, 10, 10);
        }
    }
}
