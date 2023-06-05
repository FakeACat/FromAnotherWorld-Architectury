package acats.fromanotherworld;

import acats.fromanotherworld.registry.*;
import mod.azure.azurelib.AzureLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FromAnotherWorld {
    public static final String MOD_ID = "fromanotherworld";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        AzureLib.initialize();
        ItemRegistry.register();
        DatapackRegistry.register();
    }
}
