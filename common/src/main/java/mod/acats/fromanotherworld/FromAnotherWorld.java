package mod.acats.fromanotherworld;

import mod.acats.fromanotherworld.registry.DatapackRegistry;
import mod.acats.fromanotherworld.registry.ItemRegistry;
import mod.acats.fromanotherworld.registry.SpawnEntryRegistry;
import mod.acats.fromanotherworld.utilities.interfaces.ModLoaderDependant;
import mod.azure.azurelib.AzureLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FromAnotherWorld {
    public static final String MOD_ID = "fromanotherworld";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModLoaderDependant mlDep;

    public static void init() {
        AzureLib.initialize();
        ItemRegistry.register();
        DatapackRegistry.register();
        SpawnEntryRegistry.register();
    }
}
