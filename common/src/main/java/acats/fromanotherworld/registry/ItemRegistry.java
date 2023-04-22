package acats.fromanotherworld.registry;

import acats.fromanotherworld.item.AssimilationLiquidItem;
import acats.fromanotherworld.item.GoreBottleItem;
import acats.fromanotherworld.item.ImpostorDetectorItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import java.util.HashMap;
import java.util.function.Supplier;

public class ItemRegistry {
    public static ItemGroup modItemGroup;
    public static final HashMap<String, FAWItem> ITEM_REGISTRY = new HashMap<>();
    public static final HashMap<String, FAWEgg> SPAWN_EGG_REGISTRY = new HashMap<>();
    public static class FAWEgg{
        public FAWEgg(int primaryColour, int secondaryColour, Supplier<EntityType<? extends MobEntity>> entityTypeSupplier){
            this.primaryColour = primaryColour;
            this.secondaryColour = secondaryColour;
            this.entityTypeSupplier = entityTypeSupplier;
        }
        public int primaryColour;
        public int secondaryColour;
        public Supplier<EntityType<? extends MobEntity>> entityTypeSupplier;
    }

    public static class FAWItem{
        public FAWItem(Supplier<Item> itemSupplier){
            this.itemSupplier = itemSupplier;
        }
        public final Supplier<Item> itemSupplier;
        public Item build(){
            this.item = itemSupplier.get();
            return this.item;
        }
        private Item item;
        public Item get(){
            return this.item;
        }
    }

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
    }
    private static void registerSpawnEgg(String id, int primaryColour, int secondaryColour, Supplier<EntityType<? extends MobEntity>> entityTypeSupplier){
        SPAWN_EGG_REGISTRY.put(id, new FAWEgg(primaryColour, secondaryColour, entityTypeSupplier));
    }
    public static final FAWItem ASSIMILATION_LIQUID = registerItem("assimilation_liquid", () -> new AssimilationLiquidItem(new Item.Settings().maxCount(64)));
    public static final FAWItem IMPOSTOR_DETECTOR = registerItem("impostor_detector", () -> new ImpostorDetectorItem(new Item.Settings().maxCount(1)));
    public static final FAWItem GORE_BOTTLE = registerItem("gore_bottle", () -> new GoreBottleItem(new Item.Settings().maxCount(64)));

    private static FAWItem registerItem(String id, Supplier<Item> itemSupplier){
        FAWItem fawItem = new FAWItem(itemSupplier);
        ITEM_REGISTRY.put(id, fawItem);
        return fawItem;
    }
}
