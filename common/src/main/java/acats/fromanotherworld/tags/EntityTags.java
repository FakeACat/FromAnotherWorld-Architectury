package acats.fromanotherworld.tags;

import acats.fromanotherworld.FromAnotherWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

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

    private static TagKey<EntityType<?>> entityTag(String id){
        return TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(FromAnotherWorld.MOD_ID, id));
    }
}
