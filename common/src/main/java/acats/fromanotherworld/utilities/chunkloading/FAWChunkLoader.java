package acats.fromanotherworld.utilities.chunkloading;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

public class FAWChunkLoader {
    private FAWChunkLoader(int chunkCentreX, int chunkCentreZ, int radius, int ticksUntilRemoval) {
        this.chunkCentreX = chunkCentreX;
        this.chunkCentreZ = chunkCentreZ;
        this.radius = radius;
        this.ticksUntilRemoval = ticksUntilRemoval;
    }

    CompoundTag save(CompoundTag compoundTag, int num){
        CompoundTag loaderTag = new CompoundTag();
        loaderTag.putInt("ChunkCentreX", this.chunkCentreX);
        loaderTag.putInt("ChunkCentreZ", this.chunkCentreZ);
        loaderTag.putInt("Radius", this.radius);
        loaderTag.putInt("TicksUntilRemoval", this.ticksUntilRemoval);
        compoundTag.put("ChunkLoader" + num, loaderTag);
        return compoundTag;
    }

    static FAWChunkLoader fromNBT(CompoundTag compoundTag){
        return new FAWChunkLoader(compoundTag.getInt("ChunkCentreX"),
                compoundTag.getInt("ChunkCentreZ"),
                compoundTag.getInt("Radius"),
                compoundTag.getInt("TicksUntilRemoval"));
    }

    public static void create(ServerLevel level, int x, int z, int radius, int maxAgeInMinutes){
        FAWChunkLoader chunkLoader = new FAWChunkLoader(x / 16, z / 16, radius, maxAgeInMinutes);
        chunkLoader.loadChunks(level);
        FAWChunkLoaders.getChunkLoaders(level).add(chunkLoader);
    }

    public final int chunkCentreX;
    public final int chunkCentreZ;
    public final int radius;
    public int ticksUntilRemoval;

    public void loadChunks(ServerLevel level){
        this.setForcedInRadius(level, true);
    }

    public void unloadChunks(ServerLevel level){
        this.setForcedInRadius(level, false);
    }

    private void setForcedInRadius(ServerLevel level, boolean bl){
        for (int x = this.chunkCentreX - this.radius; x <= this.chunkCentreX + this.radius; x++) {
            for (int z = this.chunkCentreZ - this.radius; z <= this.chunkCentreZ + this.radius; z++) {
                level.setChunkForced(x, z, bl);
                //if (bl) {
                //    level.startTickingChunk(level.getChunk(x, z));
                //}
            }
        }
    }
}
