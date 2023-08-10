package mod.acats.fromanotherworld.tags;

import mod.acats.fromanotherworld.FromAnotherWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class EntityTags {
    public static final TagKey<EntityType<?>> HUMANOIDS = entityTag("humanoids");
    public static final TagKey<EntityType<?>> QUADRUPEDS = entityTag("quadrupeds");
    public static final TagKey<EntityType<?>> LARGE_QUADRUPEDS = entityTag("large_quadrupeds");
    public static final TagKey<EntityType<?>> VERY_LARGE_QUADRUPEDS = entityTag("very_large_quadrupeds");
    public static final TagKey<EntityType<?>> ATTACKABLE_BUT_NOT_ASSIMILABLE = entityTag("attackable_but_not_assimilable");
    public static final TagKey<EntityType<?>> MISC = entityTag("misc");
    public static final TagKey<EntityType<?>> VILLAGERS = entityTag("villagers");
    public static final TagKey<EntityType<?>> ILLAGERS = entityTag("illagers");
    public static final TagKey<EntityType<?>> THINGS = entityTag("things");
    public static final TagKey<EntityType<?>> COWS = entityTag("cows");
    public static final TagKey<EntityType<?>> SHEEP = entityTag("sheep");
    public static final TagKey<EntityType<?>> PIGS = entityTag("pigs");
    public static final TagKey<EntityType<?>> HORSES = entityTag("horses");
    public static final TagKey<EntityType<?>> LLAMAS = entityTag("llamas");
    public static final TagKey<EntityType<?>> NOT_AFRAID_OF_THINGS = entityTag("not_afraid_of_things");
    public static final TagKey<EntityType<?>> THING_ALLIES = entityTag("thing_allies");

    private static TagKey<EntityType<?>> entityTag(String id){
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(FromAnotherWorld.MOD_ID, id));
    }
}
