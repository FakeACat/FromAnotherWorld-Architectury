package acats.fromanotherworld.utilities.chunkloading;

import acats.fromanotherworld.FromAnotherWorld;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FAWChunkLoaders extends SavedData {
    public final ArrayList<FAWChunkLoader> activeLoaders = new ArrayList<>();
    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag) {
        CompoundTag loaders = new CompoundTag();
        int i = 0;
        for (FAWChunkLoader l:
             this.activeLoaders) {
            loaders = l.save(loaders, i);
            i++;
        }
        compoundTag.put("FAWChunkLoaders", loaders);
        return compoundTag;
    }

    private static FAWChunkLoaders fromNBT(CompoundTag compoundTag){
        FAWChunkLoaders chunkLoaders = new FAWChunkLoaders();

        if (compoundTag.contains("FAWChunkLoaders")){
            CompoundTag loaders = compoundTag.getCompound("FAWChunkLoaders");
            int i = 0;
            boolean done = false;
            while (!done){
                if (loaders.contains("ChunkLoader" + i)){
                    CompoundTag loader = loaders.getCompound("ChunkLoader" + i);
                    chunkLoaders.activeLoaders.add(FAWChunkLoader.fromNBT(loader));
                    i++;
                }
                else{
                    done = true;
                }
            }
        }

        return chunkLoaders;
    }

    public static FAWChunkLoaders getChunkLoaders(ServerLevel level){
        FAWChunkLoaders chunkLoaders = level.getDataStorage().computeIfAbsent(FAWChunkLoaders::fromNBT, FAWChunkLoaders::new, FromAnotherWorld.MOD_ID + "_chunkloaders");
        chunkLoaders.setDirty();
        return chunkLoaders;
    }

    void add(FAWChunkLoader chunkLoader){
        this.activeLoaders.add(chunkLoader);
        this.setDirty();
    }

    public void tick(ServerLevel level){
        ArrayList<FAWChunkLoader> markedForRemoval = new ArrayList<>();
        for (FAWChunkLoader l:
             this.activeLoaders) {
            l.ticksUntilRemoval--;
            if (l.ticksUntilRemoval == 0) {
                l.unloadChunks(level);
                markedForRemoval.add(l);
            }
        }
        this.activeLoaders.removeAll(markedForRemoval);
        this.setDirty();
    }

    public void loadUp(ServerLevel level){
        for (FAWChunkLoader l:
             this.activeLoaders) {
            l.loadChunks(level);
        }
    }
}
