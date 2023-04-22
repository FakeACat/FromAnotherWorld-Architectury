package acats.fromanotherworld.registry;

import acats.fromanotherworld.FromAnotherWorld;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DamageTypeRegistry {
    public static final RegistryKey<DamageType> AMONG_US_POTION = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(FromAnotherWorld.MOD_ID , "among_us_potion"));
    public static DamageSource amongUsPotion(World world){
        return damageSource(world, AMONG_US_POTION);
    }

    public static final RegistryKey<DamageType> ASSIMILATION = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(FromAnotherWorld.MOD_ID , "assimilation"));
    public static DamageSource assimilation(World world) {
        return damageSource(world, ASSIMILATION);
    }

    private static DamageSource damageSource(World world, RegistryKey<DamageType> registryKey){
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(registryKey));
    }
}
