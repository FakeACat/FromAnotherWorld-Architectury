package mod.acats.fromanotherworld.entity.goal;

import mod.acats.fromanotherworld.entity.interfaces.StalkerThing;
import mod.acats.fromanotherworld.entity.thing.Thing;

import java.util.EnumSet;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;

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
        return mob.getRandom().nextInt(10) == 0 && this.stalker.findStalkTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.stalker.findStalkTarget() != null;
    }

    @Override
    public void tick() {
        Player player = this.stalker.getStalkTarget();
        if (player != null){
            if (this.mob.level().clip(new ClipContext(this.mob.getEyePosition(), player.getEyePosition(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob)).getType() == HitResult.Type.MISS) {
                this.mob.getNavigation().stop();
                double e = player.getX() - this.mob.getX();
                double f = player.getZ() - this.mob.getZ();
                this.mob.setYBodyRot(-((float) Mth.atan2(e, f)) * 57.295776F);
                this.stalker.seesTargetFromDistance();
            } else if (this.mob.getNavigation().isDone()) {
                int range = this.stalker.errorDistance();
                this.mob.getNavigation().moveTo(player.getX() + this.mob.getRandom().nextInt(2 * range + 1) - range, player.getY(), player.getZ() + this.mob.getRandom().nextInt(2 * range + 1) - range, 1.0F);
            }
        }
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        super.stop();
    }
}
