package acats.fromanotherworld.entity.goal;

import acats.fromanotherworld.entity.thing.Thing;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DirectedWanderGoal extends RandomStrollGoal {
    private final Thing thing;
    private static final float PROBABILITY = 0.001F;
    public DirectedWanderGoal(Thing thing, double d) {
        super(thing, d);
        this.thing = thing;
    }

    @Override
    public boolean canUse() {
        this.interval = (int) (120.0F * this.thing.faw$getHunger().wanderIntervalMultiplier);
        return super.canUse();
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        float distMult = thing.faw$getHunger().wanderDistMultiplier;
        if (this.mob.isInWaterOrBubble()) {
            Vec3 vec3 = LandRandomPos.getPos(this.mob, (int) (15.0F * distMult), (int) (7.0F * distMult));
            return vec3 == null ? super.getPosition() : vec3;
        } else {
            return this.mob.getRandom().nextFloat() >= PROBABILITY ? LandRandomPos.getPos(this.mob, (int) (10.0F * distMult), (int) (7.0F * distMult)) : super.getPosition();
        }
    }
}
