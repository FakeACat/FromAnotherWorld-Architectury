package mod.acats.fromanotherworld.registry;

import mod.acats.fromanotherlibrary.platform.ModLoaderSpecific;
import mod.acats.fromanotherlibrary.registry.FALRegister;
import mod.acats.fromanotherlibrary.registry.FALRegistryObject;
import mod.acats.fromanotherlibrary.registry.TabPopulator;
import mod.acats.fromanotherworld.FromAnotherWorld;
import mod.acats.fromanotherworld.item.AssimilationLiquidItem;
import mod.acats.fromanotherworld.item.FlamingArrowItem;
import mod.acats.fromanotherworld.item.GoreBottleItem;
import mod.acats.fromanotherworld.item.ImpostorDetectorItem;
import java.util.function.Supplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.*;

public class ItemRegistry {
    public static final TabPopulator TAB_POPULATOR = new TabPopulator();
    public static final FALRegister<Item> ITEM_REGISTRY = new FALRegister<>();
    public static final FALRegister<CreativeModeTab> TAB_REGISTRY = new FALRegister<>();
    public static final ResourceLocation TAB_ID = new ResourceLocation(FromAnotherWorld.MOD_ID, "from_another_world_group");
    public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, TAB_ID);
    public static final FALRegistryObject<CreativeModeTab> TAB = TAB_REGISTRY.register(TAB_ID.getPath(),
            () -> ModLoaderSpecific.INSTANCE.createTab(
                    TAB_ID,
                    () -> new ItemStack(ItemRegistry.ASSIMILATION_LIQUID.get())
            )
    );

    public static void register(){
        registerSpawnEgg("crawler_spawn_egg", 0x651619, 0x965a3f, EntityRegistry.CRAWLER::get);
        registerSpawnEgg("juliette_thing_spawn_egg", 0xbe886c, 0x8c0a19, EntityRegistry.JULIETTE_THING::get);
        registerSpawnEgg("dogbeast_spawn_egg", 0x8c0a19, 0x480a2a, EntityRegistry.DOGBEAST::get);
        registerSpawnEgg("blood_crawler_spawn_egg", 0xff0000, 0x990000, EntityRegistry.BLOOD_CRAWLER::get);
        registerSpawnEgg("dogbeast_spitter_spawn_egg", 0x480a2a, 0x8c0a19, EntityRegistry.DOGBEAST_SPITTER::get);
        registerSpawnEgg("impaler_spawn_egg", 0x8c0a19, 0x480a2a, EntityRegistry.IMPALER::get);
        registerSpawnEgg("beast_spawn_egg", 0x640827, 0x480a2a, EntityRegistry.BEAST::get);
        registerSpawnEgg("palmer_thing_spawn_egg", 0x8c0a19, 0xbe886c, EntityRegistry.PALMER_THING::get);
        registerSpawnEgg("blair_thing_spawn_egg", 0xbe886c, 0x640827, EntityRegistry.BLAIR_THING::get);
        registerSpawnEgg("alien_thing_spawn_egg", 0x0000FF, 0x000000, EntityRegistry.ALIEN_THING::get);
        registerSpawnEgg("split_face_spawn_egg", 0xbe886c, 0x8c0a19, EntityRegistry.SPLIT_FACE::get);
        registerSpawnEgg("prowler_spawn_egg", 0x640827, 0x480a2a, EntityRegistry.PROWLER::get);
    }
    private static void registerSpawnEgg(String id, int primaryColour, int secondaryColour, Supplier<EntityType<? extends Mob>> entityTypeSupplier) {
        FALRegistryObject<Item> item = register(id, () -> ModLoaderSpecific.INSTANCE.createSpawnEgg(entityTypeSupplier, primaryColour, secondaryColour));
        TAB_POPULATOR.setTabs(item::get, CreativeModeTabs.SPAWN_EGGS);
    }
    public static final FALRegistryObject<Item> ASSIMILATION_LIQUID = register("assimilation_liquid", () -> new AssimilationLiquidItem(new Item.Properties().stacksTo(64)));
    public static final FALRegistryObject<Item> IMPOSTOR_DETECTOR = register("impostor_detector", () -> new ImpostorDetectorItem(new Item.Properties().stacksTo(1)));
    public static final FALRegistryObject<Item> GORE_BOTTLE = register("gore_bottle", () -> new GoreBottleItem(new Item.Properties().stacksTo(64)));
    public static final FALRegistryObject<Item> FLAMING_ARROW = register("flaming_arrow", () -> new FlamingArrowItem(new Item.Properties().stacksTo(64)));
    public static final FALRegistryObject<Item> ALIEN_CIRCUITRY = register("alien_circuitry", () -> new Item(new Item.Properties().rarity(Rarity.EPIC).fireResistant()));

    private static FALRegistryObject<Item> register(String id, Supplier<Item> sup) {
        FALRegistryObject<Item> obj = ITEM_REGISTRY.register(id, sup);
        TAB_POPULATOR.setTabs(obj::get, TAB_KEY);
        return obj;
    }
}
