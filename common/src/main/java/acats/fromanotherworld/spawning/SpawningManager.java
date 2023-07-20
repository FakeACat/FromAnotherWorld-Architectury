package acats.fromanotherworld.spawning;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.config.Config;
import acats.fromanotherworld.entity.thing.special.AlienThing;
import acats.fromanotherworld.registry.EntityRegistry;
import acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.world.entity.MobSpawnType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.saveddata.SavedData;

public class SpawningManager extends SavedData {
    private int daysSinceLastEvent;
    private int nextEvent = Config.EVENT_CONFIG.firstEventDay.get();
    private boolean hadFirstEvent = false;
    public int alienThingsToSpawn = 0;

    public void update(ServerLevel world){
        this.daysSinceLastEvent++;
        ServerPlayer player = world.getRandomPlayer();
        if (this.daysSinceLastEvent >= this.nextEvent && world.dimensionTypeId() == BuiltinDimensionTypes.OVERWORLD && player != null){
            event(world, player);
            this.daysSinceLastEvent = 0;
            this.nextEvent = world.random.nextInt(1 + Config.EVENT_CONFIG.maxCooldown.get() - Config.EVENT_CONFIG.minCooldown.get()) + Config.EVENT_CONFIG.minCooldown.get();
        }
        this.setDirty();
    }

    @Nullable
    public ServerPlayer getRandomVictim(ServerLevel world) {
        List<ServerPlayer> list = world.getPlayers(p -> (p.isAlive() && p.canBeSeenAsEnemy()));
        return list.isEmpty() ? null : list.get(world.random.nextInt(list.size()));
    }

    public void alienThingSpawner(ServerLevel world){
        ServerPlayer player = this.getRandomVictim(world);
        if (player != null && !player.isCreative() && !player.isSpectator() && this.alienThingsToSpawn > 0){
            AlienThing alien = EntityRegistry.ALIEN_THING.get().create(world);
            if (alien != null){
                alien.changeForm(world.getRandom().nextInt(3));
                alien.finalizeSpawn(world, world.getCurrentDifficultyAt(player.blockPosition()), MobSpawnType.NATURAL, null, null);
                alien.tickEmerging();
                if (EntityUtilities.spawnOnEntityImproved(alien, world, player, 10, 20, 10, 20)){
                    this.alienThingsToSpawn--;
                    this.setDirty();
                }
            }
        }
    }

    private List<AbstractThingEvent> possibleEvents(ServerLevel world, ServerPlayer player){
        List<AbstractThingEvent> list = new ArrayList<>();
        list.add(new StarshipEvent(world, player));
        if (!this.hadFirstEvent){
            this.hadFirstEvent = true;
            return list;
        }
        list.add(new DisguisedDogEvent(world, player));
        return list;
    }

    private void event(ServerLevel world, ServerPlayer player){
        if (player != null){
            List<AbstractThingEvent> l = possibleEvents(world, player);
            AbstractThingEvent event = l.get(world.random.nextInt(l.size()));
            event.run();
        }
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag nbt) {
        nbt.putInt("DaysSinceLastEvent", this.daysSinceLastEvent);
        nbt.putInt("NextEvent", this.nextEvent);
        nbt.putBoolean("HadFirstEvent", this.hadFirstEvent);
        nbt.putInt("AlienThingsToSpawn", this.alienThingsToSpawn);
        return nbt;
    }

    public static SpawningManager fromNBT(CompoundTag nbt){
        SpawningManager spawningManager = new SpawningManager();
        spawningManager.daysSinceLastEvent = nbt.getInt("DaysSinceLastEvent");
        spawningManager.nextEvent = nbt.getInt("NextEvent");
        spawningManager.hadFirstEvent = nbt.getBoolean("HadFirstEvent");
        spawningManager.alienThingsToSpawn = nbt.getInt("AlienThingsToSpawn");
        return spawningManager;
    }

    public static SpawningManager getSpawningManager(ServerLevel world){
        SpawningManager spawningManager = world.getDataStorage().computeIfAbsent(SpawningManager::fromNBT, SpawningManager::new, FromAnotherWorld.MOD_ID);
        spawningManager.setDirty();
        return spawningManager;
    }
}
