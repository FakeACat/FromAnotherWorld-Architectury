package acats.fromanotherworld.spawning;

import acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractThingMobEvent extends AbstractThingEvent {
    public AbstractThingMobEvent(ServerWorld world, ServerPlayerEntity player){
        super(world, player);
        this.setMobs();
    }
    abstract void setMobs();
    abstract int range();
    public void addToSpawns(EntityType<? extends LivingEntity> type, int amount){
        for (int i = 0; i < amount; i++){
            LivingEntity entity = type.create(world);
            this.addToSpawns(entity);
        }
    }
    public void addToSpawns(LivingEntity entity){
        mobs.add(entity);
    }
    private final List<LivingEntity> mobs = new ArrayList<>();
    @Override
    public void run(){
        if (this.player != null){
            for (LivingEntity e:
                    mobs) {
                if (e != null) {
                    EntityUtilities.spawnOnEntity(e, world, player, range());
                }
            }
        }
        super.run();
    }
}
