package mod.acats.fromanotherworld.fabric.registry;

import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.registry.DatapackRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DatapackRegistryFabric {
    public static void register(){
        DatapackRegistry.COMPAT_DATAPACKS.forEach(DatapackRegistryFabric::registerDatapack);
    }

    private static void registerDatapack(String modID, String name){
        if (FabricLoader.getInstance().isModLoaded(modID)){
            FromAnotherWorld.LOGGER.info("Attempting to load From Another World compatibility datapack for mod: " + name);
            FabricLoader.getInstance().getModContainer(FromAnotherWorld.MOD_ID).ifPresent(modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(new ResourceLocation(FromAnotherWorld.MOD_ID, "compat_" + modID),
                    modContainer, Component.literal(name), ResourcePackActivationType.DEFAULT_ENABLED));
        }
    }
}
