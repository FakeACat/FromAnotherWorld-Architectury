package acats.fromanotherworld.forge.events;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.SpawnEntryRegistry;
import acats.fromanotherworld.utilities.registry.FAWSpawnEntry;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = FromAnotherWorld.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MiscCommonEvents {
    @SubscribeEvent
    public static void fmlCommonSetup(FMLCommonSetupEvent event){
        event.enqueueWork(() -> {
            for (FAWSpawnEntry<?> e: SpawnEntryRegistry.entries) {
                add(e);
            }
        });
    }

    private static <T extends Mob> void add(FAWSpawnEntry<T> e){
        SpawnPlacements.register(e.entityTypeSupplier().get(), e.placementType(), e.heightmapType(), e.spawnPredicate());
    }
}
