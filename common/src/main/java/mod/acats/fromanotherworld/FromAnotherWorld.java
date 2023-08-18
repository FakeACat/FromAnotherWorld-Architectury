package mod.acats.fromanotherworld;

import mod.acats.fromanotherlibrary.registry.CommonMod;
import mod.acats.fromanotherlibrary.registry.Register;
import mod.acats.fromanotherlibrary.registry.TabPopulator;
import mod.acats.fromanotherlibrary.registry.client.ClientMod;
import mod.acats.fromanotherworld.registry.BlockRegistry;
import mod.acats.fromanotherworld.registry.DatapackRegistry;
import mod.acats.fromanotherworld.registry.ItemRegistry;
import mod.acats.fromanotherworld.registry.SpawnEntryRegistry;
import mod.azure.azurelib.AzureLib;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FromAnotherWorld implements CommonMod {

    public static final String MOD_ID = "fromanotherworld";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public void init() {
        AzureLib.initialize();
        ItemRegistry.register();
        DatapackRegistry.register();
        SpawnEntryRegistry.register();

        registerEverything();
    }

    @Override
    public String getID() {
        return MOD_ID;
    }

    @Override
    public @Nullable Register<Item> getItemRegister() {
        return ItemRegistry.ITEM_REGISTRY;
    }

    @Override
    public @Nullable Register<CreativeModeTab> getTabRegister() {
        return ItemRegistry.TAB_REGISTRY;
    }

    @Override
    public @Nullable TabPopulator getTabPopulator() {
        return ItemRegistry.TAB_POPULATOR;
    }

    @Override
    public @Nullable Register<Block> getBlockRegister() {
        return BlockRegistry.BLOCK_REGISTRY;
    }

    @Override
    public @Nullable ClientMod getClientMod() {
        return new FromAnotherWorldClient();
    }
}
