package acats.fromanotherworld.tags;

import acats.fromanotherworld.FromAnotherWorld;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class DamageTypeTags {
    public static final TagKey<DamageType> ALWAYS_HURTS_THINGS = TagKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(FromAnotherWorld.MOD_ID, "always_hurts_the_thing"));
}
