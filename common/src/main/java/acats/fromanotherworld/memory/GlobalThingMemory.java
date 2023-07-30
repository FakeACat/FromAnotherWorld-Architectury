package acats.fromanotherworld.memory;

import acats.fromanotherworld.FromAnotherWorld;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;

public class GlobalThingMemory extends SavedData {
    private final ArrayList<ThingBaseOfOperations> bases = new ArrayList<>();
    public static final int BASE_RADIUS = 500;
    public static final int BASE_RADIUS_SQUARED = BASE_RADIUS * BASE_RADIUS;
    final ServerLevel level;
    private GlobalThingMemory(ServerLevel level) {
        this.level = level;
    }
    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag) {
        CompoundTag bases = new CompoundTag();
        int i = 0;
        for (ThingBaseOfOperations l:
                this.bases) {
            bases = l.toNBT(bases, i);
            i++;
        }
        compoundTag.put("ThingBases", bases);
        return compoundTag;
    }

    private static GlobalThingMemory fromNBT(CompoundTag tag, ServerLevel level) {
        GlobalThingMemory globalThingMemory = new GlobalThingMemory(level);

        if (tag.contains("ThingBases")){
            CompoundTag loaders = tag.getCompound("ThingBases");
            int i = 0;
            boolean done = false;
            while (!done){
                if (loaders.contains("Base" + i)){
                    CompoundTag base = loaders.getCompound("Base" + i);
                    globalThingMemory.bases.add(ThingBaseOfOperations.fromNBT(base, globalThingMemory));
                    i++;
                }
                else{
                    done = true;
                }
            }
        }

        return globalThingMemory;
    }

    public static GlobalThingMemory getGlobalThingMemory(ServerLevel level) {
        GlobalThingMemory globalThingMemory = level.getDataStorage().computeIfAbsent(tag -> fromNBT(tag, level), () -> new GlobalThingMemory(level), FromAnotherWorld.MOD_ID + "_thing_memory");
        globalThingMemory.setDirty();
        return globalThingMemory;
    }

    public void tick() {
        for (ThingBaseOfOperations base:
             this.bases) {
            base.director.tick();
        }
    }

    public ArrayList<ThingBaseOfOperations> getBases() {
        return this.bases;
    }

    public Optional<ThingBaseOfOperations> closestBase(int x, int y, int z) {
        ThingBaseOfOperations closest = null;
        for (ThingBaseOfOperations base:
             this.bases) {
            long d = base.sqDist(x, y, z);
            if (d < BASE_RADIUS_SQUARED && (closest == null || d < closest.sqDist(x, y, z))) {
                closest = base;
            }
        }
        return Optional.ofNullable(closest);
    }

    public long closestBaseDistSq(int x, int y, int z) {
        long dist = Long.MAX_VALUE;
        for (ThingBaseOfOperations base:
                this.bases) {
            long dist2 = base.sqDist(x, y, z);
            if (dist2 < dist) {
                dist = dist2;
            }
        }
        return dist;
    }

    public @Nullable ThingBaseOfOperations tryCreateBase(int x, int y, int z) {
        if (closestBaseDistSq(x, y, z) > BASE_RADIUS_SQUARED) {
            ThingBaseOfOperations base = new ThingBaseOfOperations(x, y, z, this);
            this.bases.add(base);
            this.setDirty();
            return base;
        }
        return null;
    }
}
