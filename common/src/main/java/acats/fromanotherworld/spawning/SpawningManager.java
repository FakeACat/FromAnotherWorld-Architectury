package acats.fromanotherworld.spawning;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.config.General;
import acats.fromanotherworld.entity.thing.special.AlienThingEntity;
import acats.fromanotherworld.registry.EntityRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.dimension.DimensionTypes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SpawningManager extends PersistentState {
    private int daysSinceLastEvent;
    private int nextEvent = General.firstEventDay;
    private boolean hadFirstEvent = false;
    public int alienThingsToSpawn = 0;

    public void update(ServerWorld world){
        this.daysSinceLastEvent++;
        ServerPlayerEntity player = world.getRandomAlivePlayer();
        if (this.daysSinceLastEvent >= this.nextEvent && world.getDimensionKey() == DimensionTypes.OVERWORLD && player != null){
            event(world, player);
            this.daysSinceLastEvent = 0;
            this.nextEvent = world.random.nextInt(1 + General.maxDaysBetweenEvents - General.minDaysBetweenEvents) + General.minDaysBetweenEvents;
        }
        this.markDirty();
    }

    @Nullable
    public ServerPlayerEntity getRandomVictim(ServerWorld world) {
        List<ServerPlayerEntity> list = world.getPlayers(p -> (p.isAlive() && p.canTakeDamage()));
        return list.isEmpty() ? null : list.get(world.random.nextInt(list.size()));
    }

    public void alienThingSpawner(ServerWorld world){
        ServerPlayerEntity player = this.getRandomVictim(world);
        if (player != null && !player.isCreative() && !player.isSpectator() && this.alienThingsToSpawn > 0){
            AlienThingEntity alien = EntityRegistry.ALIEN_THING.get().create(world);
            if (alien != null){
                alien.changeForm(world.getRandom().nextInt(3));
                alien.tickEmerging();
                if (FromAnotherWorld.spawnOnEntityImproved(alien, world, player, 10, 20, 10, 20)){
                    this.alienThingsToSpawn--;
                    this.markDirty();
                }
            }
        }
    }

    private List<AbstractThingEvent> possibleEvents(ServerWorld world, ServerPlayerEntity player){
        List<AbstractThingEvent> list = new ArrayList<>();
        list.add(new StarshipEvent(world, player));
        if (!this.hadFirstEvent){
            this.hadFirstEvent = true;
            return list;
        }
        list.add(new DisguisedDogEvent(world, player));
        return list;
    }

    private void event(ServerWorld world, ServerPlayerEntity player){
        if (player != null){
            List<AbstractThingEvent> l = possibleEvents(world, player);
            AbstractThingEvent event = l.get(world.random.nextInt(l.size()));
            event.run();
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("DaysSinceLastEvent", this.daysSinceLastEvent);
        nbt.putInt("NextEvent", this.nextEvent);
        nbt.putBoolean("HadFirstEvent", this.hadFirstEvent);
        nbt.putInt("AlienThingsToSpawn", this.alienThingsToSpawn);
        return nbt;
    }

    public static SpawningManager fromNBT(NbtCompound nbt){
        SpawningManager spawningManager = new SpawningManager();
        spawningManager.daysSinceLastEvent = nbt.getInt("DaysSinceLastEvent");
        spawningManager.nextEvent = nbt.getInt("NextEvent");
        spawningManager.hadFirstEvent = nbt.getBoolean("HadFirstEvent");
        spawningManager.alienThingsToSpawn = nbt.getInt("AlienThingsToSpawn");
        return spawningManager;
    }

    public static SpawningManager getSpawningManager(ServerWorld world){
        SpawningManager spawningManager = world.getPersistentStateManager().getOrCreate(SpawningManager::fromNBT, SpawningManager::new, FromAnotherWorld.MOD_ID);
        spawningManager.markDirty();
        return spawningManager;
    }
}
