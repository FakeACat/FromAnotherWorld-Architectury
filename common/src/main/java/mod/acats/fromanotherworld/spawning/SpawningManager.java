package mod.acats.fromanotherworld.spawning;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.config.Config;
import mod.acats.fromanotherworld.entity.interfaces.RespawnableThing;
import mod.acats.fromanotherworld.entity.thing.Thing;
import mod.acats.fromanotherworld.entity.thing.special.AlienThing;
import mod.acats.fromanotherworld.registry.EntityRegistry;
import mod.acats.fromanotherworld.utilities.EntityUtilities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.saveddata.SavedData;

public class SpawningManager extends SavedData {
    private int daysSinceLastEvent;
    private int nextEvent = Config.EVENT_CONFIG.firstEventDay.get();
    private boolean hadFirstEvent = false;

    // use thingsToSpawn instead
    @Deprecated
    public int legacyAlienThingsToSpawn = 0;

    private final List<CompoundTag> thingsToSpawn = new ArrayList<>();

    public int numThingsToSpawn() {
        return this.thingsToSpawn.size();
    }

    public void removeAndRespawnLater(Thing thing) {
        CompoundTag tag = new CompoundTag();
        thing.setRemainingFireTicks(0);
        thing.removeAllEffects();
        thing.saveAsPassenger(tag);
        if (thing instanceof RespawnableThing respawnableThing) {
            respawnableThing.modifyRespawnData(tag);
        }
        thingsToSpawn.add(tag);
        thing.discard();
        this.setDirty();
    }

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

    public void thingRespawner(ServerLevel world){
        ServerPlayer player = this.getRandomVictim(world);
        if (player != null && !player.isCreative() && !player.isSpectator()){

            if (this.legacyAlienThingsToSpawn > 0) {
                AlienThing alien = EntityRegistry.ALIEN_THING.get().create(world);
                if (alien != null){
                    alien.finalizeSpawn(world, world.getCurrentDifficultyAt(player.blockPosition()), MobSpawnType.NATURAL, null, null);
                    alien.preAttemptRespawn(player);
                    if (EntityUtilities.spawnOnEntityImproved(alien, world, player, 20, 100, 80, 20, -50)){
                        alien.postRespawn(player);
                        this.setDirty();
                    }
                }
            } else if (!this.thingsToSpawn.isEmpty()) {

                EntityType.create(this.thingsToSpawn.get(0), world).ifPresent(entity -> {

                    if (entity instanceof Thing thing) {
                        Optional<RespawnableThing> respawnableThing = thing instanceof RespawnableThing r ? Optional.of(r) : Optional.empty();
                        respawnableThing.ifPresent(respawnableThing1 -> respawnableThing1.preAttemptRespawn(player));

                        if (EntityUtilities.spawnOnEntityImproved(
                                thing,
                                world,
                                player,
                                respawnableThing.map(RespawnableThing::minHorizontalRange).orElse(20),
                                respawnableThing.map(RespawnableThing::maxHorizontalRange).orElse(100),
                                respawnableThing.map(RespawnableThing::verticalRange).orElse(80),
                                20,
                                respawnableThing.map(RespawnableThing::verticalOffset).orElse(0)
                        )) {
                            respawnableThing.ifPresent(respawnableThing1 -> respawnableThing1.postRespawn(player));

                            this.thingsToSpawn.remove(0);
                            this.setDirty();
                        }
                    }
                });
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
        nbt.putInt("AlienThingsToSpawn", this.legacyAlienThingsToSpawn);

        CompoundTag things = new CompoundTag();
        if (!this.thingsToSpawn.isEmpty()) {
            int i = 0;
            for (CompoundTag tag :
                 this.thingsToSpawn) {
                things.put("Thing" + i, tag);
                i++;
            }
        }
        nbt.put("ThingsToSpawn", things);

        return nbt;
    }

    public static SpawningManager fromNBT(CompoundTag nbt){
        SpawningManager spawningManager = new SpawningManager();
        spawningManager.daysSinceLastEvent = nbt.getInt("DaysSinceLastEvent");
        spawningManager.nextEvent = nbt.getInt("NextEvent");
        spawningManager.hadFirstEvent = nbt.getBoolean("HadFirstEvent");
        spawningManager.legacyAlienThingsToSpawn = nbt.getInt("AlienThingsToSpawn");

        if (nbt.contains("ThingsToSpawn")) {
            CompoundTag things = nbt.getCompound("ThingsToSpawn");
            int i = 0;
            while (things.contains("Thing" + i)) {
                spawningManager.thingsToSpawn.add(things.getCompound("Thing" + i));
                i++;
            }
        }

        return spawningManager;
    }

    public static SpawningManager getSpawningManager(ServerLevel world){
        SpawningManager spawningManager = world.getDataStorage().computeIfAbsent(SpawningManager::fromNBT, SpawningManager::new, FromAnotherWorld.MOD_ID);
        spawningManager.setDirty();
        return spawningManager;
    }
}
