package mod.acats.fromanotherworld.registry;

import mod.acats.fromanotherworld.FromAnotherWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class DamageTypeRegistry {
    public static final ResourceKey<DamageType> AMONG_US_POTION = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(FromAnotherWorld.MOD_ID , "among_us_potion"));
    public static DamageSource amongUsPotion(Level world){
        return damageSource(world, AMONG_US_POTION);
    }

    public static final ResourceKey<DamageType> ASSIMILATION = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(FromAnotherWorld.MOD_ID , "assimilation"));
    public static DamageSource assimilation(Level world) {
        return damageSource(world, ASSIMILATION);
    }

    private static DamageSource damageSource(Level world, ResourceKey<DamageType> registryKey){
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(registryKey));
    }
}
