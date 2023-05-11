package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.thing.special.AlienThingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class StalkGoal extends Goal {
    protected final AlienThingEntity mob;
    public StalkGoal(AlienThingEntity mob){
        this.mob = mob;
        this.setControls(EnumSet.of(Control.MOVE));
    }
    @Override
    public boolean canStart() {
        return mob.getRandom().nextInt(40) == 0 && mob.getStalkTarget() != null;
    }

    @Override
    public boolean shouldContinue() {
        return !this.mob.getNavigation().isIdle();
    }

    @Override
    public void start() {
        int range = 64;
        PlayerEntity player = this.mob.getStalkTarget();
        if (player != null){
            this.mob.getNavigation().startMovingTo(player.getX() + this.mob.getRandom().nextInt(2 * range + 1) - range, player.getY(), player.getZ() + this.mob.getRandom().nextInt(2 * range + 1) - range, 1.0F);
        }
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        super.stop();
    }
}
