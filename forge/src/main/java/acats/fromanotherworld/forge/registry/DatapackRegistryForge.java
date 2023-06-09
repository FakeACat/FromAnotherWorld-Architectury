package acats.fromanotherworld.forge.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.DatapackRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.resource.PathPackResources;

import java.nio.file.Path;

public class DatapackRegistryForge {
    public static void register(final AddPackFindersEvent event){
        if (event.getPackType() == PackType.SERVER_DATA){
            DatapackRegistry.COMPAT_DATAPACKS.forEach((modID, name) -> registerDatapack(modID, name, event));
        }
    }

    private static void registerDatapack(String modID, String name, final AddPackFindersEvent event){
        if (ModList.get().isLoaded(modID)){
            FromAnotherWorld.LOGGER.info("Attempting to load From Another World compatibility datapack for mod: " + name);
            Path resourcePath = ModList.get().getModFileById(FromAnotherWorld.MOD_ID).getFile().findResource("resourcepacks/compat_" + modID);
            Pack pack = Pack.readMetaAndCreate("builtin/compat_" + modID, Component.literal("Assimilated " + name), false,
            (path) -> new PathPackResources(path, false, resourcePath), PackType.SERVER_DATA, Pack.Position.BOTTOM, PackSource.BUILT_IN);
            event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
        }
    }
}
