package acats.fromanotherworld.tags;

import acats.fromanotherworld.FromAnotherWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class DamageTypeTags {
    public static final TagKey<DamageType> ALWAYS_HURTS_THINGS = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(FromAnotherWorld.MOD_ID, "always_hurts_the_thing"));
}
