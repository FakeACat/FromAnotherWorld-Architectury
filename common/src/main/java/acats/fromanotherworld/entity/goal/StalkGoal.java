package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.interfaces.StalkerThing;
import acats.fromanotherworld.entity.thing.Thing;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class StalkGoal extends Goal {
    protected final Thing mob;
    protected final StalkerThing stalker;
    public StalkGoal(StalkerThing stalker){
        this.stalker = stalker;
        this.mob = (Thing) stalker;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }
    @Override
    public boolean canUse() {
        return mob.getRandom().nextInt(40) == 0 && this.stalker.findStalkTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        int range = 64;
        Player player = this.stalker.findStalkTarget();
        if (player != null){
            this.mob.getNavigation().moveTo(player.getX() + this.mob.getRandom().nextInt(2 * range + 1) - range, player.getY(), player.getZ() + this.mob.getRandom().nextInt(2 * range + 1) - range, 1.0F);
        }
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        super.stop();
    }
}
