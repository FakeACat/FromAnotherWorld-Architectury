package mod.acats.fromanotherworld.spawning;

import mod.acats.fromanotherworld.utilities.EntityUtilities;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public abstract class AbstractThingMobEvent extends AbstractThingEvent {
    public AbstractThingMobEvent(ServerLevel world, ServerPlayer player){
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
