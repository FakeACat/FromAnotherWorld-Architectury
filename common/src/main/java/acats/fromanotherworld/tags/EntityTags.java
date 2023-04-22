package acats.fromanotherworld.tags;

import acats.fromanotherworld.FromAnotherWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class EntityTags {
    public static final TagKey<EntityType<?>> HUMANOIDS = entityCategory("humanoids");
    public static final TagKey<EntityType<?>> LARGE_QUADRUPEDS = entityCategory("large_quadrupeds");
    public static final TagKey<EntityType<?>> QUADRUPEDS = entityCategory("quadrupeds");
    public static final TagKey<EntityType<?>> SMALL = entityCategory("small");
    public static final TagKey<EntityType<?>> ATTACKABLE_BUT_NOT_ASSIMILABLE = entityCategory("attackable_but_not_assimilable");

    private static TagKey<EntityType<?>> entityCategory(String id){
        return TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(FromAnotherWorld.MOD_ID, id));
    }
}
