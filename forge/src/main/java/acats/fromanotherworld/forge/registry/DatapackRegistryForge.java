package acats.fromanotherworld.forge.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.registry.DatapackRegistry;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.resource.PathPackResources;

import java.nio.file.Path;

public class DatapackRegistryForge {
    public static void register(final AddPackFindersEvent event){
        if (event.getPackType() == ResourceType.SERVER_DATA){
            DatapackRegistry.COMPAT_DATAPACKS.forEach((modID, name) -> registerDatapack(modID, name, event));
        }
    }

    private static void registerDatapack(String modID, String name, final AddPackFindersEvent event){
        if (ModList.get().isLoaded(modID)){
            FromAnotherWorld.LOGGER.info("Attempting to load From Another World compatibility datapack for mod: " + name);
            Path resourcePath = ModList.get().getModFileById(FromAnotherWorld.MOD_ID).getFile().findResource("resourcepacks/compat_" + modID);
            ResourcePackProfile pack = ResourcePackProfile.create("builtin/compat_" + modID, Text.literal("Assimilated " + name), false,
            (path) -> new PathPackResources(path, false, resourcePath), ResourceType.SERVER_DATA, ResourcePackProfile.InsertionPosition.BOTTOM, ResourcePackSource.BUILTIN);
            event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
        }
    }
}
